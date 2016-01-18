package es.chg.portal.requests.pfirma.wsclient.query;

public class QueryServiceProxy implements es.chg.portal.requests.pfirma.wsclient.query.QueryService_PortType {
  private String _endpoint = null;
  private es.chg.portal.requests.pfirma.wsclient.query.QueryService_PortType queryService_PortType = null;
  
  public QueryServiceProxy() {
    _initQueryServiceProxy();
  }
  
  public QueryServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initQueryServiceProxy();
  }
  
  private void _initQueryServiceProxy() {
    try {
      queryService_PortType = (new es.chg.portal.requests.pfirma.wsclient.query.QueryService_ServiceLocator()).getQueryServicePort();
      if (queryService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)queryService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)queryService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (queryService_PortType != null)
      ((javax.xml.rpc.Stub)queryService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.chg.portal.requests.pfirma.wsclient.query.QueryService_PortType getQueryService_PortType() {
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType;
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.Signature downloadSign(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.downloadSign(authentication, documentId);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.User[] queryUsers(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryUsers(authentication, query);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.DocumentType[] queryDocumentTypes(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryDocumentTypes(authentication, query);
  }
  
  public byte[] downloadDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.downloadDocument(authentication, documentId);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.State[] queryStates(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryStates(authentication, query);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.ImportanceLevel[] queryImportanceLevels(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryImportanceLevels(authentication, query);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.Request queryRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryRequest(authentication, requestId);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.Job[] queryJobs(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryJobs(authentication, query);
  }
  
  public java.lang.String getCVS(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Signature firma) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.getCVS(authentication, firma);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUser[] queryEnhancedUsers(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String queryUser, java.lang.String querySeat) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryEnhancedUsers(authentication, queryUser, querySeat);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.EnhancedJob[] queryEnhancedJobs(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String queryJob, java.lang.String querySeat) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryEnhancedJobs(authentication, queryJob, querySeat);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.Seat[] querySeats(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String query) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.querySeats(authentication, query);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUserJobAssociated[] queryEnhancedUserJobAssociatedToJob(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String jobIdentifier) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryEnhancedUserJobAssociatedToJob(authentication, jobIdentifier);
  }
  
  public es.chg.portal.requests.pfirma.wsclient.type.EnhancedUserJobAssociated[] queryEnhancedUserJobAssociatedToUser(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String userIdentifier) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (queryService_PortType == null)
      _initQueryServiceProxy();
    return queryService_PortType.queryEnhancedUserJobAssociatedToUser(authentication, userIdentifier);
  }
  
  
}