/**
 * QueryService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.chg.portal.requests.pfirma.wsclient.query;

public interface QueryService_PortType extends java.rmi.Remote {
    public es.chg.portal.requests.pfirma.wsclient.type.Signature downloadSign(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.User[] queryUsers(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.DocumentType[] queryDocumentTypes(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public byte[] downloadDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.State[] queryStates(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.ImportanceLevel[] queryImportanceLevels(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.Request queryRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.Job[] queryJobs(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public java.lang.String getCVS(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Signature firma) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUser[] queryEnhancedUsers(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String queryUser, java.lang.String querySeat) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.EnhancedJob[] queryEnhancedJobs(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String queryJob, java.lang.String querySeat) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.Seat[] querySeats(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUserJobAssociated[] queryEnhancedUserJobAssociatedToJob(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String jobIdentifier) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
    public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUserJobAssociated[] queryEnhancedUserJobAssociatedToUser(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String userIdentifier) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo;
}
