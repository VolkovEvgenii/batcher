package ru.volkov.batch.util.db;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceDb {

    private final EntityManager manager;

    @Autowired
    public ServiceDb(EntityManager manager) {
        this.manager = manager;
    }


    /**
     * Возвращает список объектов типа: {@link StPrigorodRjdCard} (хранит в себе номера чипа UID карты) для которых одновременно выполняются следующие условия:
     * <p>
     *     <ul>
     *         <li>Код меры социальной поддержки равен "stPrigorodRJD"</li>
     *         <li>Дата начала действия меры социальной поддержки меньше текущей</li>
     *         <li>Дата окончания действия меры социальной поддержки равна null или больше текущей</li>
     *     </ul>
     * </p>
     * @param date текущая дата относительно которой вычисляются условия, not null
     * @return список {@link StPrigorodRjdCard}
     */
    public List<StPrigorodRjdCard> getCardsWithActiveRjd(LocalDateTime date) {

        String selectCardsWithPrigorodRjd = "" +
                "select h.card_bsk_num \n" +
                "from esrn.nominated_payment np \n" +
                "inner join esrn.esrnpayment e on np.esrn_payment_payment_code = e.payment_code \n" +
                "inner join main.holders h on np.card_holder_pers_id = h.pers_id \n" +
                "where np.open_date < :date \n" +
                "and (np.close_date is null or np.close_date > :date) \n" +
                "and e.payment_code = 'stPrigorodRJD'";

        Query query = manager.createNativeQuery(selectCardsWithPrigorodRjd);
        query.setParameter("date", date);

        return query
                .unwrap(org.hibernate.Query.class)
                .setResultTransformer(Transformers.aliasToBean(StPrigorodRjdCard.class))
                .list();
    }

}
