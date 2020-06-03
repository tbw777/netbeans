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
package org.netbeans.modules.cnd.makeproject.api.wizards;

import java.io.IOException;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.util.ConnectionManager;
import org.netbeans.modules.nativeexecution.api.util.HostInfoUtils;
import org.openide.util.Lookup;

/**
 *
 */
public abstract class DefaultMakeProjectLocationProvider {

    private static final DefaultMakeProjectLocationProvider EMPTY = new Empty();

    protected DefaultMakeProjectLocationProvider() {
    }

    public static DefaultMakeProjectLocationProvider getDefault() {
        DefaultMakeProjectLocationProvider provider = Lookup.getDefault().lookup(DefaultMakeProjectLocationProvider.class);
        return provider == null ? EMPTY : provider;
    }

    public abstract String getDefaultProjectFolder();

    public abstract String getDefaultProjectFolder(ExecutionEnvironment env);

    private static final class Empty extends DefaultMakeProjectLocationProvider {

        Empty() {
        }

        @Override
        public String getDefaultProjectFolder() {
            return System.getProperty("user.home"); //NOI18N
        }

        @Override
        public String getDefaultProjectFolder(ExecutionEnvironment env) {
            try {
                if (env.isLocal()) {
                    return getDefaultProjectFolder();
                } else {
                    return HostInfoUtils.getHostInfo(env).getUserDir();
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err); // it doesn't make sense to disturb user
            } catch (ConnectionManager.CancellationException ex) {
                ex.printStackTrace(System.err); // it doesn't make sense to disturb user
            }
            return null;
        }

    }
}