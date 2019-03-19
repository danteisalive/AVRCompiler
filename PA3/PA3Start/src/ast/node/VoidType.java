/* This file was generated by SableCC (http://www.sablecc.org/).  
 * Then modified.
 */
package ast.node;

import ast.visitor.*;

@SuppressWarnings("nls")
public final class VoidType extends IType
{

    public VoidType(int _line_, int _pos_)
    {
        super(_line_, _pos_);
    }

    @Override
    public int getNumExpChildren() { return 0; }
    
    @Override
    public Object clone()
    {
        return new VoidType(            
                this.getLine(),
                this.getPos());
    }

    public void accept(Visitor v)
    {
        v.visitVoidType(this);
    }

    @Override
    void removeChild(Node child)
    {
        // Remove child
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(Node oldChild, Node newChild)
    {
        // Replace child
        throw new RuntimeException("Not a child.");
    }
}