/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;

/**
 *
 * @author misanchez
 */
@Stateless
public class OfertaRepo extends AbstractRepository<OfertaBienesServicio, Long> {

    public OfertaRepo() {
        super(OfertaBienesServicio.class);
    }

}
