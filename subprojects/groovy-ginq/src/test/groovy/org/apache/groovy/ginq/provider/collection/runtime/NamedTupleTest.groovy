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
package org.apache.groovy.ginq.provider.collection.runtime


import groovy.transform.CompileStatic
import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

@CompileStatic
class NamedTupleTest {
    @Test
    void testToString() {
        assert '(a:1, b:2, c:3)' == new NamedTuple([1, 2, 3], ['a', 'b', 'c']).toString()
    }

    @Test
    void testDuplicatedNames() {
        def err = shouldFail '''
            import org.apache.groovy.ginq.provider.collection.runtime.NamedTuple

            new NamedTuple([1, 2, 3], ['a', 'a', 'c'])
        '''

        assert err.toString().contains('names should be unique')
    }

    @Test
    void testElementsAndNamesSizeMismatch() {
        def err = shouldFail '''
            import org.apache.groovy.ginq.provider.collection.runtime.NamedTuple

            new NamedTuple([1, 2, 3], ['a', 'b'])
        '''

        assert err.toString().contains('elements(size: 3) and names(size: 2) should have the same size')
    }
}
