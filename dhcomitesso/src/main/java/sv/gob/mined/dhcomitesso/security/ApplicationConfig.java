/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.security;


/**
 *
 * @author hantsy
 */
//@DatabaseIdentityStoreDefinition(
//        dataSourceLookup = "java:/SieCssoDS",
//        callerQuery = "select user_password from users where username = ?",
//        groupsQuery = "select user_group from grupo where username = ?",
//        hashAlgorithm = Pbkdf2PasswordHash.class,
//        priorityExpression = "#{100}",
//        hashAlgorithmParameters = {
//            "Pbkdf2PasswordHash.Iterations=3072",
//            "${applicationConfig.dyna}"
//        } // just for test / example
//)
//@CustomFormAuthenticationMechanismDefinition(
//        loginToContinue = @LoginToContinue(
//                loginPage = "/inicio.xhtml",
//                errorPage = "" // DRAFT API - must be set to empty for now
//        )
//)
//@ApplicationScoped
//@Named
public class ApplicationConfig {

    public String[] getDyna() {
        return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
    }

}
