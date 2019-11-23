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
package org.codehaus.groovy.classgen.asm;

import org.codehaus.groovy.control.BytecodeProcessor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * An utility class which can be used in test cases to dump generated bytecode.
 *
 * @since 2.4.0
 */
public class BytecodeDumper implements BytecodeProcessor {
    public static final BytecodeDumper STANDARD_ERR = new BytecodeDumper(new PrintWriter(System.err));

    private final Writer out;

    public BytecodeDumper(final Writer out) {
        this.out = out;
    }

    @Override
    public byte[] processBytecode(final String name, final byte[] original) {
        PrintWriter pw = out instanceof PrintWriter ? (PrintWriter) out : new PrintWriter(out);
        TraceClassVisitor visitor = new TraceClassVisitor(pw);
        ClassReader reader = new ClassReader(original);
        reader.accept(visitor, CompilerConfiguration.READ_MODE);
        return original;
    }

}
