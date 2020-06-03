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

package org.netbeans.modules.cnd.debugger.dbx.breakpoints.types;


import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.NativeBreakpoint;
import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.BreakpointPanel;
import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.NativeBreakpointType;

//@BreakpointType.Registration(displayName="#LBL_Fault")
public class FaultBreakpointType extends NativeBreakpointType {

    // interface BreakpointType
    public NativeBreakpoint newInstance(int flags) {
	return new FaultBreakpoint(flags);
    } 

    // interface BreakpointType
    @Override
    public String getTypeDisplayName() {
        return Catalog.get("LBL_Fault");
    }

    // interface NativeBreakpointType
    public BreakpointPanel getCustomizer(NativeBreakpoint editable) {
	if (editable == null)
	    return new FaultBreakpointPanel();
	else
	    return new FaultBreakpointPanel(editable);
    }
    
    @Override
    public String id() {
        return "Fault"; //NOI18N
    }
}