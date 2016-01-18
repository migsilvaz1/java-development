package es.chg.portal.requests.pfirma.wsclient.modify;

public class ModifyServiceProxy implements es.chg.portal.requests.pfirma.wsclient.modify.ModifyService_PortType {
  private String _endpoint = null;
  private es.chg.portal.requests.pfirma.wsclient.modify.ModifyService_PortType modifyService_PortType = null;
  
  public ModifyServiceProxy() {
    _initModifyServiceProxy();
  }
  
  public ModifyServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initModifyServiceProxy();
  }
  
  private void _initModifyServiceProxy() {
    try {
      modifyService_PortType = (new es.chg.portal.requests.pfirma.wsclient.modify.ModifyService_ServiceLocator()).getModifyServicePort();
      if (modifyService_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)modifyService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)modifyService_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (modifyService_PortType != null)
      ((javax.xml.rpc.Stub)modifyService_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.chg.portal.requests.pfirma.wsclient.modify.ModifyService_PortType getModifyService_PortType() {
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    return modifyService_PortType;
  }
  
  public java.lang.String createRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Request request) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    return modifyService_PortType.createRequest(authentication, request);
  }
  
  public java.lang.String updateRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, es.chg.portal.requests.pfirma.wsclient.type.Request request) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    return modifyService_PortType.updateRequest(authentication, request);
  }
  
  public void deleteRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.deleteRequest(authentication, requestId);
  }
  
  public void removeRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, java.lang.String removingMessage) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.removeRequest(authentication, requestId, removingMessage);
  }
  
  public void sendRequest(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.sendRequest(authentication, requestId);
  }
  
  public java.lang.String insertDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, java.lang.String requestId, es.chg.portal.requests.pfirma.wsclient.type.Document document) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    return modifyService_PortType.insertDocument(authentication, requestId, document);
  }
  
  public void deleteDocument(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder documentId) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.deleteDocument(authentication, documentId);
  }
  
  public void insertSigners(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, int signLine, es.chg.portal.requests.pfirma.wsclient.type.Signer[] signerList, es.chg.portal.requests.pfirma.wsclient.type.SignLineType signLineType) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.insertSigners(authentication, requestId, signLine, signerList, signLineType);
  }
  
  public void deleteSigners(es.chg.portal.requests.pfirma.wsclient.type.Authentication authentication, javax.xml.rpc.holders.StringHolder requestId, java.lang.Integer signLineNumber, es.chg.portal.requests.pfirma.wsclient.type.Signer[] signerList) throws java.rmi.RemoteException, es.chg.portal.requests.pfirma.wsclient.type.ExceptionInfo{
    if (modifyService_PortType == null)
      _initModifyServiceProxy();
    modifyService_PortType.deleteSigners(authentication, requestId, signLineNumber, signerList);
  }
  
  
}