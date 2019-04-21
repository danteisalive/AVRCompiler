package symtable;

public class ClassSTE extends STE{

	private boolean mMain;
	private String mSuperClass;
	private Scope mScope;
	private Integer mSize;

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

	public String getClassName(){
		return getSTEName();
	}

	public Integer getClassSize(){
		return mSize;
	}
	public String toString(){

			return " <f0> ClassSTE | <f1> mName = " + getSTEName() +
			"| <f2> mMain = " + String.valueOf(mMain) +
			"| <f3> mSuperClass = " + mSuperClass +
			"| <f4> mScope ";

	}




}
