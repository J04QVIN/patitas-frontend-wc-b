package pe.edu.cibertec.patitasfrontendwcb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.patitasfrontendwcb.config.FeignClientConfig;
import pe.edu.cibertec.patitasfrontendwcb.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitasfrontendwcb.dto.LogoutResponseDTO;

@FeignClient(name="logout", url="http://localhost:8081/autenticacion",configuration = FeignClientConfig.class)
public interface LogoutClient {

    @PostMapping("/logout")
    ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO);
}