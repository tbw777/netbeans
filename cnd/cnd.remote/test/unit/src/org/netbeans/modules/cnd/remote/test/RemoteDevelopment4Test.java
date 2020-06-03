/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.cnd.remote.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.netbeans.modules.cnd.remote.full.FullRemoteBuildTestCase;
import org.netbeans.modules.cnd.remote.pbuild.LinuxTouchRfsBuildTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RemoteBuildLinksTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RemoteBuildMakefileTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RemoteBuildRequiredProjectsTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RemoteBuildSamplesTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RfsGnuRemoteBuildTestCase;
import org.netbeans.modules.cnd.remote.pbuild.RfsSunStudioRemoteBuildTestCase;
import org.netbeans.modules.cnd.remote.pbuild.SftpGnuRemoteBuildTestCase;
import org.netbeans.modules.cnd.remote.pbuild.SftpSunStudioRemoteBuildTestCase;
import org.netbeans.modules.cnd.test.CndBaseTestSuite;

/**
 *
 */
public class RemoteDevelopment4Test extends CndBaseTestSuite {

   public RemoteDevelopment4Test() {
       this("Remote Development", // NOI18N           
           RfsSunStudioRemoteBuildTestCase.class,
           SftpSunStudioRemoteBuildTestCase.class,
           LinuxTouchRfsBuildTestCase.class,
           RemoteBuildLinksTestCase.class,
           RemoteBuildRequiredProjectsTestCase.class,
           RemoteBuildSamplesTestCase.class,
           RfsGnuRemoteBuildTestCase.class,
           SftpGnuRemoteBuildTestCase.class,
           RemoteBuildMakefileTestCase.class,
           FullRemoteBuildTestCase.class
       );
   }

    private RemoteDevelopment4Test(String name, Class... testClasses) {
        super(name, "remote.platforms", testClasses);
    }

    public static Test suite() {
        TestSuite suite = new RemoteDevelopment4Test();
        return suite;
    }
}