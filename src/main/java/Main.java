import br.com.gwenilorac.biblioteca.app.client.ApplicationFrm;
import br.com.gwenilorac.biblioteca.app.client.LoginFrm;

public class Main {
	
	public static void main(String[] args) {
		LoginFrm loginFrm = new LoginFrm();
		
		if (loginFrm.isOK()) {
			new ApplicationFrm();
		}
		System.out.println("TESTE");
	}

}