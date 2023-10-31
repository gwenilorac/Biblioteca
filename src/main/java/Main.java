import br.com.gwenilorac.biblioteca.app.client.ApplicationFrm;
import br.com.gwenilorac.biblioteca.app.client.LoginECadastro;
import br.com.gwenilorac.biblioteca.app.client.LoginFrm;

public class Main {
	
	public static void main(String[] args) {
//		LoginECadastro loginEcadastro = new LoginECadastro();
		
//		CadastroFrm cadastroFrm = new CadastroFrm(); 
		
		LoginFrm loginFrm = new LoginFrm();
		
		if (loginFrm.isOK()) {
			new ApplicationFrm();
		}
		System.out.println("TESTE");
	}

}
