/**
 *
 */
package com.smacna.model;

/**
 * @author sumit.v
 * @version 1.0
 */
public class JsonResponse {
    private String status = null;
    private Object result = null;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }

}
