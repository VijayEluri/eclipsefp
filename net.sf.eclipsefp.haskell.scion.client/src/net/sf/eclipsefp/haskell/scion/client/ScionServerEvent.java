package net.sf.eclipsefp.haskell.scion.client;

import java.util.EventObject;

import net.sf.eclipsefp.haskell.scion.internal.servers.ScionServer;

public class ScionServerEvent extends EventObject {
  /** Required serial version UID. */
  private static final long serialVersionUID = 1032029311500515018L;
  /** The event type enumeration */
  ScionServerEventType      evType;
  /** The scion-server associated with the event source's {@link ScionInstance}. */
  ScionServer               server;

  /**
   * Construct a scion-server event
   * 
   * @param source
   *          The {@link ScionInstance} that caused this event
   * @param server
   *          The {@link ScionServer} associated with the ScionInstance. 
   * @param evType
   *          The event type of the status change
   */
  public ScionServerEvent(ScionInstance source, ScionServer server, ScionServerEventType evType) {
    super(source);
    this.evType = evType;
    this.server = server;
  }

  /** Get the event type */
  public ScionServerEventType getEventType() {
    return evType;
  }

  /** Get the scion-server */
  public ScionServer getScionServer() {
    return server;
  }
}
