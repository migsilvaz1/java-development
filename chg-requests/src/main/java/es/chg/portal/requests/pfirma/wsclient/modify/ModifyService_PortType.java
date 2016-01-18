/**
 * ModifyService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.chg.portal.requests.pfirma.wsclient.modify;

public interface ModifyService_PortType extends java.rmi.Remote {
    public java.lang.String createRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Request request) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public java.lang.String updateRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Request request) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void deleteRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void removeRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, java.lang.String removingMessage) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void sendRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public java.lang.String insertDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String requestId, es.chg.portal.requests.pfirma.wsclient.type.Document document) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void deleteDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void insertSigners(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, int signLine, es.chg.portal.requests.pfirma.wsclient.type.Signer[] signerList, es.chg.portal.requests.pfirma.wsclient.type.SignLineType signLineType) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public void deleteSigners(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, java.lang.Integer signLineNumber, es.chg.portal.requests.pfirma.wsclient.type.Signer[] signerList) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
}
