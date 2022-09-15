/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author hantsy
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:/PaqueteDS",
        callerQuery = "select clave_acceso from persona where usuario = ?",
        groupsQuery = "select tusu.rol from persona per inner join usuario usu on usu.id_persona = per.id_persona inner join tipo_usuario tusu on usu.id_tipo_usuario = tusu.id_tipo_usuario where per.usuario = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        priorityExpression = "#{100}",
        hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "${applicationConfig.dyna}"
        } // just for test / example
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/inicio.xhtml",
                errorPage = "/access.xhtml", // DRAFT API - must be set to empty for now
                useForwardToLogin = false,
                useForwardToLoginExpression = ""
        )
)
@ApplicationScoped
@Named
public class ApplicationConfig {

    public String[] getDyna() {
        return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
    }

}
