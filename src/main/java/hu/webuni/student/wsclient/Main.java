package hu.webuni.student.wsclient;

public class Main {
    public static void main(String[] args) {

        CentralsystemXmlWs centralsystemXmlWsImplPort = new CentralsystemXmlWsImplService().getCentralsystemXmlWsImplPort(); // a port az interface szinonimaja a wsdl-ben
        System.out.println(centralsystemXmlWsImplPort.getFreeSemesterByCentralId(1));


    }
}
