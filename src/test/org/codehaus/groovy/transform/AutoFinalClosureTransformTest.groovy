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
package org.codehaus.groovy.transform

import gls.CompilableTestSupport

import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


/**
 * Tests for the {@code @AutoFinal} AST transform.
 */

@RunWith(JUnit4)
class AutoFinalClosureTransformTest extends CompilableTestSupport {

    // Execute single test:
    // gradlew :test --build-cache --tests org.codehaus.groovy.transform.AutoFinalClosureTransformTest
    void testAutoFinalOnClass() {
        //throw new Exception("AutoFinalClosureTransformTest#testAutoFinalOnClass FAILED BY DESIGN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        // use ASTTest here since final modifier isn't put into bytecode so not available via reflection
        assertScript '''
            import groovy.transform.AutoFinal
            import groovy.transform.ASTTest
            import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
            import static java.lang.reflect.Modifier.isFinal

            @ASTTest(phase=SEMANTIC_ANALYSIS, value = {
                assert node.methods.size() == 1
                node.methods[0].with {
                    assert it.name == 'fullName'
                    assert it.parameters.every{ p -> isFinal(p.modifiers) }
                }
                assert node.constructors.size() == 1
                node.constructors[0].with {
                    assert it.parameters.every{ p -> isFinal(p.modifiers) }
                }
            })
            @AutoFinal
            class Person {
                final String first, last
                Person(String first, String last) {
                    this.first = first
                    this.last = last
                }
                String fullName(boolean reversed = false, String separator = ' ') {
                    reversed = true
                    seperator = '<!#!>'
                    "${reversed ? last : first}$separator${reversed ? first : last}"
                }
            }

            final js = new Person('John', 'Smith')
            assert js.fullName() == 'John Smith'
            assert js.fullName(true, ', ') == 'Smith, John'
        '''
    }

    @Test
    void testAutoFinalOnClass2() {
        // 1) ASTTest explicitely checks for final modifier (which isn't put into bytecode)
        // 2) shouldNotCompile checks that the Groovy compiler responds in the expected way to an attempt at assigning a value to a method parameter
        final result = shouldNotCompile('''
            import groovy.transform.AutoFinal
            import groovy.transform.ASTTest
            import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
            import static java.lang.reflect.Modifier.isFinal

            @ASTTest(phase=SEMANTIC_ANALYSIS, value = {
                assert node.methods.size() == 1
                node.methods[0].with {
                    assert it.name == 'fullName'
                    assert it.parameters.every{ p -> isFinal(p.modifiers) }
                }
                assert node.constructors.size() == 1
                node.constructors[0].with {
                    assert it.parameters.every{ p -> isFinal(p.modifiers) }
                }
            })
            @AutoFinal
            class Person {
                final String first, last
                Person(String first, String last) {
                    this.first = first
                    this.last = last
                }
                String fullName(boolean reversed = false, String separator = ' ') {
                    reversed = true
                    seperator = '<!#!>'
                    "${reversed ? last : first}$separator${reversed ? first : last}"
                }
            }

            final js = new Person('John', 'Smith')
            assert js.fullName() == 'John Smith'
            assert js.fullName(true, ', ') == 'Smith, John'
        ''')
        //println "\n\nAutoFinalClosureTransformTest#testAutoFinalOnClass2 result: |$result|\n\n"
        assert result.contains('The parameter [reversed] is declared final but is reassigned')
    }
}
