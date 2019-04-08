package symtable;

import exceptions.*;

import java.util.*;

public class Type
{
  public static final Type BOOL = new Type();
  public static final Type INT = new Type();
  public static final Type BYTE = new Type();
  public static final Type COLOR = new Type();
  public static final Type BUTTON = new Type();
  public static final Type VOID = new Type();
  public static final Type TONE = new Type();
  public static final Type INTARRAY = new Type();
  public static final Type COLORARRAY = new Type();

  private final String className;

  private Type()
  {
    className = null;
  }

  public Type(String id)
  {
    className = id;
  }

  public boolean isClassType(){
    if (className != null)  return true;
    else                    return false;
  }

  public String getClassName(){

    return className;

  }
  public String toString()
  {

    if (className != null)
    {
      return "class_" + className;
    }

    if(this == INT)
    {
      return "INT";
    }

    if(this == BOOL)
    {
      return "BOOL";
    }

    if(this == BYTE)
    {
      return "BYTE";
    }

    if(this == COLOR)
    {
      return "COLOR";
    }

    if(this == BUTTON)
    {
      return "BUTTON";
    }

    if(this == TONE)
    {
      return "TONE";
    }

    if (this == VOID)
    {
      return "class_null";
    }

    if (this == INTARRAY)
    {
      return "INTARRAY";
    }

    if (this == COLORARRAY)
    {
      return "COLORARRAY";
    }

    return "MAINCLASS;";
  }

  public int getAVRTypeSize() {
      //if (className != null) { throw new InternalException("This VarSTE size is not defined!\n");}
      if(this == INT) { return 2; }
      if(this == BOOL) { return 1; }
      if(this == BYTE) { return 1; }
      if(this == COLOR) { return 1; }
      if(this == BUTTON) { return 1; }
      if(this == VOID) { return 0; }
      if(this == TONE) { return 2;}
      if(this == INTARRAY) {return 2;}
      if(this == COLORARRAY) {return 2;}
      return 2; // class references are 2 bytes
  }


}
