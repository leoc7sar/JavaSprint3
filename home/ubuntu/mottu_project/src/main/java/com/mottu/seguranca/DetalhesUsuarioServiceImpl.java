package com.mottu.seguranca;

import com.mottu.seguranca.UsuarioRepositorio;
import com.mottu.seguranca.UsuarioPapelRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
public class DetalhesUsuarioServiceImpl implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioPapelRepositorio usuarioPapelRepositorio;

    public DetalhesUsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio, UsuarioPapelRepositorio usuarioPapelRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioPapelRepositorio = usuarioPapelRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        var authorities = usuarioPapelRepositorio.findByUsuario(usuario).stream()
                .map(usuarioPapel -> new SimpleGrantedAuthority(usuarioPapel.getPapel().getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isEnabled(),
                true, true, true,
                authorities
        );
    }
}

