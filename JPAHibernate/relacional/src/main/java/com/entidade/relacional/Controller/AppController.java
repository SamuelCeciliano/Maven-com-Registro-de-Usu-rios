package com.entidade.relacional.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.entidade.relacional.model.AppUser;
import com.entidade.relacional.model.Profissao;
import com.entidade.relacional.repository.AppUserRepository;
import com.entidade.relacional.repository.ProfissaoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @GetMapping("/users")
    public String getMethodName(Model model) {
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("profissoes", profissaoRepository.findAll());

        return "users";
    }

    @PostMapping("addUser")
    public String postMethodName(@RequestParam String nome, @RequestParam Long profissaoId) {
        AppUser user = new AppUser();
        user.setNome(nome);
        Profissao profissao = profissaoRepository.findById(profissaoId).orElse(null);

        if (profissao != null) {
            user.setProfissao(profissao);
            appUserRepository.save(user);

        }

        return "redirect:/users";
    }

    @PostMapping("/addProfissao")
    public String addProfissao(@RequestParam String titulo) {

        Profissao profissao = new Profissao();
        profissao.setTitulo(titulo);
        profissaoRepository.save(profissao);

        return "redirect:/users";
    }







    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        // Buscar o usuário pelo ID
        AppUser user = appUserRepository.findById(id).orElse(null);

        // Se o usuário não existir, retornar uma página de erro
        if (user == null) {
            return "redirect:/users"; // Redirecionar de volta à lista de usuários
        }

        // Adicionar o usuário e as profissões ao modelo
        model.addAttribute("user", user);
        model.addAttribute("profissoes", profissaoRepository.findAll());

        // Retornar o nome da página de edição (editar-user.html)
        return "editar-user";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam Long id, @RequestParam String nome, @RequestParam Long profissaoId) {
        // Buscar o usuário pelo ID
        AppUser user = appUserRepository.findById(id).orElse(null);

        // Verificar se o usuário existe
        if (user != null) {
            // Atualizar o nome do usuário
            user.setNome(nome);

            // Buscar a profissão pelo ID e atualizar
            Profissao profissao = profissaoRepository.findById(profissaoId).orElse(null);
            if (profissao != null) {
                user.setProfissao(profissao);
            }

            // Salvar o usuário atualizado no banco de dados
            appUserRepository.save(user);
        }

        // Redirecionar de volta à lista de usuários
        return "redirect:/users";
    }

    // Método para exportar usuários em CSV
    @RequestMapping(value = "/exportCSV", method = RequestMethod.GET)
    public ResponseEntity<String> exportCSV() {
        List<AppUser> users = appUserRepository.findAll();
        
        // Cabeçalho CSV
        StringBuilder csvContent = new StringBuilder("ID,Nome,Profissão\n");

        // Adicionar dados de cada usuário no formato CSV
        for (AppUser user : users) {
            csvContent.append(user.getId())
                    .append(",")
                    .append(user.getNome())
                    .append(",")
                    .append(user.getProfissao() != null ? user.getProfissao().getTitulo() : "")
                    .append("\n");
        }

        // Definir cabeçalhos para o arquivo de resposta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=usuarios.csv");

        return new ResponseEntity<>(csvContent.toString(), headers, HttpStatus.OK);
    }

    // Método para exportar usuários em TXT
    @RequestMapping(value = "/exportTXT", method = RequestMethod.GET)
    public ResponseEntity<String> exportTXT() {
        List<AppUser> users = appUserRepository.findAll();
        
        // Criar o conteúdo do arquivo TXT
        StringBuilder txtContent = new StringBuilder("ID - Nome - Profissão\n");

        // Adicionar dados de cada usuário no formato TXT
        for (AppUser user : users) {
            txtContent.append(user.getId())
                    .append(" - ")
                    .append(user.getNome())
                    .append(" - ")
                    .append(user.getProfissao() != null ? user.getProfissao().getTitulo() : "")
                    .append("\n");
        }

        // Definir cabeçalhos para o arquivo de resposta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=usuarios.txt");

        return new ResponseEntity<>(txtContent.toString(), headers, HttpStatus.OK);
    }
}
