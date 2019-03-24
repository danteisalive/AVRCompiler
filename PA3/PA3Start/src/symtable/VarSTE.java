package symtable;

public class VarSTE extends STE{

	private Type mType;
	private int mOffset;
	private String mBase;

	public VarSTE(String mName, Type mType, String mBase, int mOffset){
		super(mName);
		this.mType = mType;
		this.mBase = mBase;  // "Y" if method variable "Z" if class variable
		this.mOffset = mOffset;
	}

	public Type getSTEType(){
		return mType;
	}

	public int getSTEOffset(){
		return mOffset;
	}

	public String getSTEBase(){
		return mBase;
	}


}
