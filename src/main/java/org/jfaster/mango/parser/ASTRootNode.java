package org.jfaster.mango.parser;

import org.jfaster.mango.operator.InvocationContext;
import org.jfaster.mango.operator.ParameterContext;
import org.jfaster.mango.parser.visitor.*;
import org.jfaster.mango.util.SQLType;

import java.util.List;

public class ASTRootNode extends AbstractRenderableNode {

    private NodeInfo nodeInfo = new NodeInfo();

    public ASTRootNode(int id) {
        super(id);
    }

    public ASTRootNode(Parser p, int id) {
        super(p, id);
    }

    public ASTRootNode init() {
        getBlock().jjtAccept(TextBlankJoinVisitor.INSTANCE, null);
        getBlock().jjtAccept(InterablePropertyCollectVisitor.INSTANCE, null);
        getBlock().jjtAccept(NodeCollectVisitor.INSTANCE, nodeInfo);
        return this;
    }

    @Override
    public boolean render(InvocationContext context) {
        ((AbstractRenderableNode) getDDLNode()).render(context);
        ((AbstractRenderableNode) getBlock()).render(context);
        return true;
    }

    public SQLType getSQLType() {
        SQLType sqlType;
        if (getDDLNode() instanceof ASTInsert) {
            sqlType = SQLType.INSERT;
        } else if (getDDLNode() instanceof ASTDelete) {
            sqlType = SQLType.DELETE;
        } else if (getDDLNode() instanceof ASTUpdate) {
            sqlType = SQLType.UPDATE;
        } else if (getDDLNode() instanceof ASTSelect) {
            sqlType = SQLType.SELECT;
        } else {
            throw new IllegalStateException();
        }
        return sqlType;
    }

    /**
     * 扩展简化的参数节点
     */
    public void expandParameter(ParameterContext context) {
        getBlock().jjtAccept(ParameterExpandVisitor.INSTANCE, context);
    }

    /**
     * 绑定GetterInvoker
     */
    public void bindInvoker(ParameterContext context) {
        getBlock().jjtAccept(CheckAndBindVisitor.INSTANCE, context);
    }

    public List<ASTJDBCParameter> getJDBCParameters() {
        return nodeInfo.jdbcParameters;
    }

    public List<ASTJDBCIterableParameter> getJDBCIterableParameters() {
        return nodeInfo.jdbcIterableParameters;
    }

    public List<ASTGlobalTable> getASTGlobalTables() {
        return nodeInfo.globalTables;
    }

    private Node getDDLNode() {
        return jjtGetChild(0);
    }

    private Node getBlock() {
        return jjtGetChild(1);
    }

}