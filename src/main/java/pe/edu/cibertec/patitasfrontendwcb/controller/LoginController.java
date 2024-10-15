package pe.edu.cibertec.patitasfrontendwcb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitasfrontendwcb.dto.LoginRequestDTO;
import pe.edu.cibertec.patitasfrontendwcb.dto.LoginResponseDTO;
import pe.edu.cibertec.patitasfrontendwcb.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    WebClient webClientAutenticacion;


    @GetMapping("/inicio")
    public String inicio(Model model){
        LoginModel loginModel = new LoginModel("00","","");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {
        //validar campos de entrada
        if(tipoDocumento == null || tipoDocumento.trim().length()==0
                || numeroDocumento == null || numeroDocumento.trim().length()==0
                || password == null || password.trim().length()==0){
            LoginModel loginModel = new LoginModel("01","Error: Debe completar correctamente sus credenciales","");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        try {

            //Invocar Api
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

            Mono<LoginResponseDTO> response = webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginResponseDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class);

            //recuperar resultado del mono (Sincrono o bloqueante)
            LoginResponseDTO loginResponseDTO = response.block();


            //Validar respuesta
            if(loginResponseDTO.codigo().equals("00")){
                LoginModel loginModel = new LoginModel("00","",loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";

            }else{
                LoginModel loginModel = new LoginModel("02","Error: Autenticacion fallida","");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }
        } catch (Exception e) {
            LoginModel loginModel = new LoginModel("99","Error: Ocurrio un problema en la autenticacion","");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";
        }


    }
}