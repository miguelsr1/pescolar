/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.util.validator;

import org.primefaces.validate.bean.AbstractClientValidationConstraint;

/**
 *
 * @author misanchez
 */
public class EmailClientValidationConstraint extends AbstractClientValidationConstraint {

    public static final String MESSAGE_METADATA = "data-p-email-msg";

    public EmailClientValidationConstraint() {
        super(null, MESSAGE_METADATA);
    }

    @Override
    public String getValidatorId() {
        return Email.class.getSimpleName();
    }

}
