package symtable;

public class VarSTE extends STE{

	private Type mType;
	private int mOffset;
	private String mBase; 
	
	public VarSTE(String mName, Type mType){
		super(mName);
		this.mType = mType;
		this.mBase = "Y"; 
		this.mOffset = 1;
	}		
	
	public VarSTE(String mName, Type mType, int mOffset){
		super(mName);
		this.mType = mType;
		this.mBase = "Y"; 
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
