package com.hss01248.webviewlib;
import com.just.agentweb.AgentWeb;

import uk.co.alt236.webviewdebug.BaseJsObj;

public class BaseAgentJsObj extends BaseJsObj {
    protected AgentWeb agentWeb;

    public void set(AgentWeb agentWeb){
        this.agentWeb = agentWeb;
    }
}
