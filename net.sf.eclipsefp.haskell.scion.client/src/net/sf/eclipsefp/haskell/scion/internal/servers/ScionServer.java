package net.sf.eclipsefp.haskell.scion.internal.servers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.eclipsefp.haskell.scion.client.ScionPlugin;
import net.sf.eclipsefp.haskell.scion.exceptions.ScionServerStartupException;
import net.sf.eclipsefp.haskell.scion.internal.commands.ConnectionInfoCommand;
import net.sf.eclipsefp.haskell.scion.internal.commands.ScionCommand;
import net.sf.eclipsefp.haskell.scion.internal.util.Trace;
import net.sf.eclipsefp.haskell.scion.internal.util.ScionText;
import net.sf.eclipsefp.haskell.util.PlatformUtil;

import org.eclipse.core.runtime.IPath;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWTException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * some helper code to implement a ScionServer
 * 
 * @author JP Moresmau
 * 
 */
public abstract class ScionServer {
  protected static final String              CLASS_PREFIX         = "[ScionServer]";
  protected static final String              SERVER_STDOUT_PREFIX = "[scion-server]";
  /** Message prefix for commands send to the server */
  protected static final String              TO_SERVER_PREFIX     = "[scion-server] << ";
  /** Message prefix for responses received from the server */
  protected static final String              FROM_SERVER_PREFIX   = "[scion-server] >> ";
  /** The Scion protocol version number */
  private static final String                PROTOCOL_VERSION     = "0.1";
  /**
   * Path to the server executable that is started and with whom EclipseFP
   * communicates
   */
  protected IPath                            serverExecutable;
  /**
   * Project name, used to disambiguate server from each other in the tracing
   * output.
   */
  protected final String                     projectName;
  /** Server logging output stream, generally tends to be a Eclipse console */
  protected Writer                           serverOutput;
  /** Working directory where the server operates, can be null if no project. */
  protected File                             directory;
  /** The scion-server process */
  protected Process                          process;
  /** scion-server's output stream, read by EclipseFP */
  protected BufferedReader                   serverOutStream;
  /** scion-server's input stream, written to by EclipseFP */
  protected BufferedWriter                   serverInStream;
  /** Request identifier */
  private final AtomicInteger                nextSequenceNumber;

  /** logs output **/
  protected OutputWriter outputWriter;
  
  /** Command queue, to deal with both synchronous and asynchronous commands */
  protected final Map<Integer, ScionCommand> commandQueue;

  /**
   * The constructor
   * 
   * @param projectName
   *          TODO
   * @param serverExecutable
   *          The scion-server executable
   * @param serverOutput
   *          The scion-server's logging and trace stream.
   * @param directory
   *          The scion-server's working directory
   */
  public ScionServer(String projectName, IPath serverExecutable, Writer serverOutput, File directory) {
    this.projectName = projectName;
    this.serverExecutable = serverExecutable;
    this.serverOutput = serverOutput;
    this.directory = directory;

    this.process = null;
    this.serverOutStream = null;
    this.serverInStream = null;
    this.nextSequenceNumber = new AtomicInteger(1);
    this.commandQueue = new HashMap<Integer, ScionCommand>();
  }

  /**
   * The default constructor. This is only used by NullScionServer
   * 
   * @param projectName
   *          TODO
   */
  protected ScionServer() {
    this.projectName = ScionText.noproject;
    this.serverExecutable = null;
    this.serverOutput = null;
    this.directory = null;

    this.process = null;
    this.serverOutStream = null;
    this.serverInStream = null;
    this.nextSequenceNumber = new AtomicInteger(1);
    this.commandQueue = new HashMap<Integer, ScionCommand>();
  }

  /** Redirect the logging stream */
  public void setOutputStream(final Writer outStream) {
    serverOutput = outStream;
  }

  /**
   * Start the server process.
   * 
   * @note This method should not be overridden by subclasses. Subclasses should
   *       override {@link doStartServer doStartServer} instead. This is a
   *       protocol design method, where {@link ScionServer ScionServer}
   *       implements code that must be executed before or after the subclass'
   *       server launch.
   */
  public final void startServer() throws ScionServerStartupException {
    Trace.trace(CLASS_PREFIX, "Starting server " + getClass().getSimpleName() + ":" + projectName);
    outputWriter=new OutputWriter(getClass().getSimpleName() + "/"
			+ projectName+"/"+"Writer");
    outputWriter.start();
    doStartServer(projectName);
    Trace.trace(CLASS_PREFIX, "Server started for " + getClass().getSimpleName() + ":" + projectName);
  }

  /**
   * Subclass' hook for starting up their respective server processes.
   * 
   * @param projectName
   *          The project name
   */
  protected void doStartServer(String projectName) throws ScionServerStartupException {
    // Does nothing...
    serverOutStream = null;
    serverInStream = null;
  }

  /**
   * Stop the server process.
   * 
   * @note This method should not be overridden by subclasses. Subclasses should
   *       override {@link doStopServer doStopServer} instead. This is a
   *       protocol design method, where {@link ScionServer ScionServer}
   *       implements code that must be executed before or after the subclass'
   *       server launch.
   */
  public final void stopServer() {
    Trace.trace(CLASS_PREFIX, "Stopping server");

    try {
       // Let the subclass do its thing. BEFORE we close the streams, to give
       // the sub class a chance to close its own things properly
       doStopServer();
        
	  outputWriter.setTerminate();
	  outputWriter.interrupt(); 
       
      if (serverOutStream != null) {
        serverOutStream.close();
        serverOutStream = null;
      }
      if (serverInStream != null) {
        serverInStream.close();
        serverInStream = null;
      }
      

      // Then kill off the server process.
      if (process != null) {
        process.destroy();
        process = null;
      }
    } catch (Throwable ex) {
      // ignore
    }

    Trace.trace(CLASS_PREFIX, "Server stopped");
  }

  /**
   * Subclass' hook for stopping their respective server processes.
   */
  protected void doStopServer() {
    // Base class does nothing.
  }

  /**
   * Send a command to the server, but do not wait for a response (asynchronous
   * version).
   */
  public void sendCommand(ScionCommand command) {
    if (serverInStream != null) {
      // Keep track of this request in the command queue
      int seqNo = nextSequenceNumber.getAndIncrement();
      command.setSequenceNumber(seqNo);

      synchronized (commandQueue) {
        commandQueue.put(new Integer(seqNo), command);
      }

      String jsonString = command.toJSONString();

      try {
        serverInStream.write(command.toJSONString() + PlatformUtil.NL);
        serverInStream.flush();
      } catch (IOException ex) {
        //try {
          outputWriter.addMessage(getClass().getSimpleName() + ".sendCommand encountered an exception:");
          outputWriter.addMessage(ex);
          //serverOutput.write(getClass().getSimpleName() + ".sendCommand encountered an exception:" + PlatformUtil.NL);
//          ex.printStackTrace(new PrintWriter(serverOutput));
          //serverOutput.flush();
//        } catch (IOException e) {
//          stopServer();
//        }
      } finally {
        final String toServer = projectName + "/" + TO_SERVER_PREFIX;
       
        if (Trace.isTracing()) {
        	Trace.trace(toServer, "%s", jsonString);
        	outputWriter.addMessage(TO_SERVER_PREFIX + jsonString);
//          try {
//            serverOutput.write(TO_SERVER_PREFIX + jsonString + PlatformUtil.NL);
//            serverOutput.flush();
//          } catch (IOException ex) {
//            // Ignore this (something creative here?) 
//          } catch (SWTException se){
//        	  // device is disposed when shutting down, we hope
//          }
        }
      }
    }
  }

  /**
   * Send a command, wait for its response.
   * 
   * @return true if the command completed successfully, otherwise false.
   */
  public final boolean sendCommandSync(ScionCommand command) {
    boolean retval = false;

    if (serverInStream != null) {
      command.setIsSync();

      synchronized (command) {
        sendCommand(command);
        while (command.isWaiting()) {
          try {
            command.wait();
          } catch (InterruptedException ex) {
            // We'll spin until the request is processed...
          }
        }
      }
      retval = command.isDone();
      if (retval){
    	  // we are in the calling thread, this is safe
    	  command.runSuccessors(ScionServer.this);
      }
    }
    return retval;
  }

  /**
   * Check the server's protocol version. This just generates a warning if the
   * version numbers do not match.
   */
  public void checkProtocol() {
    sendCommand(new ConnectionInfoCommand());
  }

  /**
   * Parses the given response string and stores the command result in this
   * object.
   */
  public boolean processResponse(JSONObject response) {
    boolean retval = false;
    int id = response.optInt("id", -1);
    ScionCommand command = null;

    // Ensure command is always dequeued
    synchronized (commandQueue) {
      Integer key = new Integer(id);
      command = commandQueue.remove(key);
    }

    if (!checkResponseVersion(response)) {
      return retval;
    } else if (id <= 0) {
      // Command identifiers are always greater than 0.
      logMessage(ScionText.errorReadingId_warning, null);
      return retval;
    } else if (command == null) {
      // Should have found the command in the queue...
      logMessage(NLS.bind(ScionText.commandIdMismatch_warning, id), null);
      return retval;
    } else {
      Object result = response.opt("result");
      if (result != null) {
        try {
          command.setResponse(response);
          command.processResult(result);
          command.setCommandDone();
          // we're not in the calling thread!!
          if (!command.isSync()){
        	  command.runSuccessors(ScionServer.this);
          }
          retval = true;
        } catch (JSONException jsonex) {
          command.setCommandError();
          logMessage(ScionText.commandProcessingFailed_message, jsonex);
        } finally {
          command.setResponse(response);
        }
      } else {
        JSONObject error = response.optJSONObject("error");
        if (error != null) {
          try {
            String name = error.getString("name");
            String message = error.getString("message");
            if (!command.onError(name, message)) {
              logMessage(NLS.bind(ScionText.commandError_message, name, message), null);
            }
            command.setCommandError();
          } catch (JSONException ex2) {
            command.setCommandError();
            logMessage(ScionText.commandProcessingFailed_message, ex2);
          }
        } else {
          command.setCommandError();
          logMessage(NLS.bind(ScionText.commandErrorMissing_message, result), null);
        }
      }
    }

    return retval;
  }

  /**
   * Log a message to both the scion-server's output stream and the Eclipse log
   * file.
   * 
   * @param message
   *          The error message to log
   * @param exc
   *          The exception associated with the message, may be null.
   */
  private void logMessage(final String message, final Throwable exc) {
//    try {
//      serverOutput.write(message + PlatformUtil.NL);
//      serverOutput.flush();
//    } catch (IOException ioex) {
//      // Not much we can do.
//    }
	outputWriter.addMessage(message);
	  
    ScionPlugin.logError(message, exc);
  }

  /**
   * Check the response from scion-server, ensure that the version number
   * matches. Otherwise, yell at the user.
   */
  private boolean checkResponseVersion(JSONObject response) {
    boolean retval = true;
    String version = response.optString("version");

    if (version == null) {
      logMessage(ScionText.errorReadingVersion_warning, null);
      retval = false;
    } else if (!version.equals(PROTOCOL_VERSION)) {
      ScionPlugin.logWarning(NLS.bind(ScionText.commandVersionMismatch_warning, version, PROTOCOL_VERSION), null);
      retval = false;
    }

    return retval;
  }
  
  /**
   * A separate thread to write the communication with the server
   * see https://bugs.eclipse.org/bugs/show_bug.cgi?id=259107
   */
	public class OutputWriter extends Thread {
		/** the message list **/
		private final LinkedList<String> messages = new LinkedList<String>();
		/** shoulw we stop? **/
		private boolean terminateFlag;
		/** marker from things coming from the server		 */
		private final String fromServer = projectName + "/" + FROM_SERVER_PREFIX;
		
		public OutputWriter(String name) {
			super(name);
		}

		public void setTerminate() {
			terminateFlag = true;
		}

		public void addMessage(String msg) {
			synchronized (messages) {
				messages.add(msg);
				messages.notify();
			}
		}
		
		public void addMessage(char[] buf, int start,int length) {
			synchronized (messages) {
				messages.add(new String(buf,start,length));
				messages.notify();
			}
		}
		
		public void addMessage(Exception e) {
			StringWriter sw=new StringWriter();
			PrintWriter pw=new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			synchronized (messages) {
				messages.add(sw.toString());
				messages.notify();
			}
		}

		@Override
		public void run() {
			while (!terminateFlag && serverOutput!=null){
				String m=null;
				synchronized (messages) {
					try {
						while (messages.isEmpty()){
							messages.wait();
						}
						
					} catch (InterruptedException ignore){
						// noop
					}
					if (!messages.isEmpty()){
						m=messages.removeFirst();
					}
				}
				if (m!=null){
					Trace.trace(fromServer,m);
					try {
						serverOutput.write(m + PlatformUtil.NL);
						serverOutput.flush();
					} catch (IOException ex) {
						if (!terminateFlag) {
							ScionPlugin.logError(
									ScionText.scionServerNotRunning_message, ex);
						}
					} catch (SWTException se) {
						// probably device has been disposed
						if (!terminateFlag) {
							ScionPlugin.logError(
									ScionText.scionServerNotRunning_message, se);
						}
					}
				}
			}
		}

	}
}
