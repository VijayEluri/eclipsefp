/* Generated By:JavaCC: Do not edit this line. OutputParserTokenManager.java */
package net.sf.eclipsefp.haskell.hlint.parser;
import java.util.*;
import net.sf.eclipsefp.haskell.hlint.*;

/** Token Manager. */
public class OutputParserTokenManager implements OutputParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1f80L) != 0L)
         {
            jjmatchedKind = 6;
            return -1;
         }
         if ((active0 & 0x8L) != 0L)
            return 6;
         return -1;
      case 1:
         if ((active0 & 0x1f80L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 2:
         if ((active0 & 0x1f80L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 3:
         if ((active0 & 0x1f80L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 4:
         if ((active0 & 0x1f80L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 5:
         if ((active0 & 0x780L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 6:
         if ((active0 & 0x780L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 7:
         if ((active0 & 0x780L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 8:
         if ((active0 & 0x680L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 9:
         if ((active0 & 0x680L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 10:
         if ((active0 & 0x680L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 11:
         if ((active0 & 0x480L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      case 12:
         if ((active0 & 0x480L) != 0L)
         {
            if (jjmatchedPos == 0)
            {
               jjmatchedKind = 6;
               jjmatchedPos = 0;
            }
            return -1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 32:
         return jjStopAtPos(0, 1);
      case 58:
         return jjStartNfaWithStates_0(0, 3, 6);
      case 67:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 69:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 78:
         return jjMoveStringLiteralDfa1_0(0x1080L);
      case 80:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x100L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 103:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x1880L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 32:
         return jjMoveStringLiteralDfa3_0(active0, 0x80L);
      case 100:
         return jjMoveStringLiteralDfa3_0(active0, 0x800L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x600L);
      case 116:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa4_0(active0, 0x1800L);
      case 111:
         return jjMoveStringLiteralDfa4_0(active0, 0x500L);
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x280L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 58:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(4, 11);
         else if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(4, 12);
         break;
      case 101:
         return jjMoveStringLiteralDfa5_0(active0, 0x200L);
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x500L);
      case 117:
         return jjMoveStringLiteralDfa5_0(active0, 0x80L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 32:
         return jjMoveStringLiteralDfa6_0(active0, 0x600L);
      case 101:
         return jjMoveStringLiteralDfa6_0(active0, 0x100L);
      case 103:
         return jjMoveStringLiteralDfa6_0(active0, 0x80L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa7_0(active0, 0x100L);
      case 101:
         return jjMoveStringLiteralDfa7_0(active0, 0x200L);
      case 103:
         return jjMoveStringLiteralDfa7_0(active0, 0x80L);
      case 109:
         return jjMoveStringLiteralDfa7_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 41:
         if ((active0 & 0x100L) != 0L)
            return jjStopAtPos(7, 8);
         break;
      case 101:
         return jjMoveStringLiteralDfa8_0(active0, 0x480L);
      case 114:
         return jjMoveStringLiteralDfa8_0(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjMoveStringLiteralDfa8_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 114:
         return jjMoveStringLiteralDfa9_0(active0, 0x200L);
      case 115:
         return jjMoveStringLiteralDfa9_0(active0, 0x480L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private int jjMoveStringLiteralDfa9_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 111:
         return jjMoveStringLiteralDfa10_0(active0, 0x200L);
      case 115:
         return jjMoveStringLiteralDfa10_0(active0, 0x400L);
      case 116:
         return jjMoveStringLiteralDfa10_0(active0, 0x80L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private int jjMoveStringLiteralDfa10_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa11_0(active0, 0x400L);
      case 105:
         return jjMoveStringLiteralDfa11_0(active0, 0x80L);
      case 114:
         if ((active0 & 0x200L) != 0L)
            return jjStopAtPos(10, 9);
         break;
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
}
private int jjMoveStringLiteralDfa11_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(9, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
   }
   switch(curChar)
   {
      case 103:
         return jjMoveStringLiteralDfa12_0(active0, 0x400L);
      case 111:
         return jjMoveStringLiteralDfa12_0(active0, 0x80L);
      default :
         break;
   }
   return jjStartNfa_0(10, active0);
}
private int jjMoveStringLiteralDfa12_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(10, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(11, active0);
      return 12;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa13_0(active0, 0x400L);
      case 110:
         return jjMoveStringLiteralDfa13_0(active0, 0x80L);
      default :
         break;
   }
   return jjStartNfa_0(11, active0);
}
private int jjMoveStringLiteralDfa13_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(11, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(12, active0);
      return 13;
   }
   switch(curChar)
   {
      case 58:
         if ((active0 & 0x400L) != 0L)
            return jjStopAtPos(13, 10);
         break;
      case 115:
         if ((active0 & 0x80L) != 0L)
            return jjStopAtPos(13, 7);
         break;
      default :
         break;
   }
   return jjStartNfa_0(12, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 8;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0xfbffffffffffdbffL & l) != 0L)
                  {
                     if (kind > 6)
                        kind = 6;
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 2)
                        kind = 2;
                  }
                  else if (curChar == 58)
                     jjAddStates(0, 1);
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 5)
                        kind = 5;
                     jjCheckNAdd(3);
                  }
                  else if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 6:
                  if (curChar == 47 && kind > 4)
                     kind = 4;
                  break;
               case 1:
                  if (curChar == 10 && kind > 2)
                     kind = 2;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 3:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(3);
                  break;
               case 4:
                  if ((0xfbffffffffffdbffL & l) != 0L && kind > 6)
                     kind = 6;
                  break;
               case 5:
                  if (curChar == 58)
                     jjAddStates(0, 1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (kind > 6)
                     kind = 6;
                  break;
               case 6:
                  if (curChar == 92 && kind > 4)
                     kind = 4;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((jjbitVec0[i2] & l2) != 0L && kind > 6)
                     kind = 6;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 8 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   6, 7, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", "\40", null, "\72", null, null, null, 
"\116\157\40\163\165\147\147\145\163\164\151\157\156\163", "\151\147\156\157\162\145\144\51", 
"\120\141\162\163\145\40\145\162\162\157\162", "\105\162\162\157\162\40\155\145\163\163\141\147\145\72", 
"\103\157\144\145\72", "\116\157\164\145\72", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[8];
private final int[] jjstateSet = new int[16];
protected char curChar;
/** Constructor. */
public OutputParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public OutputParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 8; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
         matchedToken = jjFillToken();
         return matchedToken;
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

}
