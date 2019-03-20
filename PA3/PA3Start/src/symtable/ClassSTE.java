package symtable;

public class ClassSTE extends STE{

	private boolean mMain;
	private String mSuperClass;
	private Scope mScope;

	public ClassSTE(String mName, boolean mMain, String mSuperClass, Scope mScope){
		super(mName);
		this.mMain = mMain;
		this.mSuperClass = mSuperClass;
		this.mScope = mScope;
	}

	public void setScope(Scope scope){
		mScope = scope;
	}

	public Scope getScope(){
		return mScope;
	}

	public String toString(){
		return " mName: " + getSTEName() + " mMain: " + String.valueOf(mMain) + " mSuperClass: " + mSuperClass;
	}



}
