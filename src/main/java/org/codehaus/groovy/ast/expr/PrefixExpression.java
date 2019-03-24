/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.ast.expr;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.syntax.Token;

/**
 * Represents a prefix expression like ++foo or --bar
 */
public class PrefixExpression extends Expression {

    private final Token operation;
    private Expression expression;

    public PrefixExpression(Token operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    public String toString() {
        return super.toString() + "[" + operation + expression + "]";
    }

    public void accept(GroovyCodeVisitor visitor) {
        visitor.visitPrefixExpression(this);
    }

    public Expression transformExpression(ExpressionTransformer transformer) {
        Expression ret = new PrefixExpression(operation, transformer.transform(expression)); 
        ret.setSourcePosition(this);
        ret.copyNodeMetaData(this);
        return ret;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Token getOperation() {
        return operation;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getText() {
        return "(" + operation.getText() + expression.getText() + ")";
    }

    public ClassNode getType() {
        return expression.getType();
    }

}
