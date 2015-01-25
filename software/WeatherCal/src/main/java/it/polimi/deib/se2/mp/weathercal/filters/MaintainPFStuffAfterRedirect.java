/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.filters;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.FilterConfig;
import javax.servlet.ServletResponse;
import org.primefaces.context.RequestContext;

/**
 *
 * @author paolo
 */
public class MaintainPFStuffAfterRedirect implements PhaseListener {

    private static final long serialVersionUID = 1250469273857785274L;
    private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";
    private static final String sessionToken2 = "MULTI_PAGE_DIALOG_SUPPORT";

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    /*
     * Check to see if we are "naturally" in the RENDER_RESPONSE phase. If we
     * have arrived here and the response is already complete, then the page is
     * not going to show up: don't display messages yet.
     */
    @Override
    public void beforePhase(final PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        int msg = this.saveStuff(facesContext);

        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            if (!facesContext.getResponseComplete()) {
                this.restoreStuff(facesContext);
            }
        }
    }

    /*
     * Save messages into the session after every phase.
     */
    @Override
    public void afterPhase(final PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES ||
                event.getPhaseId() == PhaseId.PROCESS_VALIDATIONS ||
                event.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
            FacesContext facesContext = event.getFacesContext();
            int msg = this.saveStuff(facesContext);
        }
    }

    @SuppressWarnings("unchecked")
    private int saveStuff(final FacesContext facesContext) {
        List<FacesMessage> messages = new ArrayList<>();
        for (Iterator<FacesMessage> iter = facesContext.getMessages(null); iter.hasNext();) {
            messages.add(iter.next());
            iter.remove();
        }
        List<String> scriptsToExecute = new ArrayList<>();
        for (Iterator<String> iter = RequestContext.getCurrentInstance().getScriptsToExecute().iterator(); iter.hasNext();) {
            scriptsToExecute.add(iter.next());
            iter.remove();
        }
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        if (!messages.isEmpty()) {
            List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap.get(sessionToken);
            if (existingMessages != null) {
                existingMessages.addAll(messages);
            } else {
                sessionMap.put(sessionToken, messages);
            }
        }
        if (!scriptsToExecute.isEmpty()) {
            List<String> existingScripts = (List<String>) sessionMap.get(sessionToken2);
            if (existingScripts != null) {
                existingScripts.addAll(scriptsToExecute);
            } else {
                sessionMap.put(sessionToken2, scriptsToExecute);
            }
        }
        return messages.size() + scriptsToExecute.size();
    }

    @SuppressWarnings("unchecked")
    private int restoreStuff(final FacesContext facesContext) {
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        List<FacesMessage> messages = (List<FacesMessage>) sessionMap.remove(sessionToken);
        List<String> scriptsToExecute = (List<String>) sessionMap.remove(sessionToken2);

        if (messages != null) {
            messages.stream().forEach((element) -> {
                facesContext.addMessage(null, (FacesMessage) element);
            });
        }

        if (scriptsToExecute != null) {
            RequestContext rq = RequestContext.getCurrentInstance();
            scriptsToExecute.stream().forEach((element) -> {
                rq.execute((String) element);
            });
        }
        return (messages == null? 0: messages.size()) + (scriptsToExecute == null? 0: scriptsToExecute.size());
    }
    
    public MaintainPFStuffAfterRedirect() {
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
}