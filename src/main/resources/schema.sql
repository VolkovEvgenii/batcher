DROP TABLE IF EXISTS esrn.esrnpayment CASCADE ;
DROP TABLE IF EXISTS main.holders CASCADE ;
DROP TABLE IF EXISTS esrn.nominated_payment CASCADE ;


CREATE TABLE IF NOT EXISTS main.holders (
    pers_id             SERIAL,
    card_bsk_num        VARCHAR(20),
    PRIMARY KEY (pers_id)
);

CREATE TABLE IF NOT EXISTS esrn.esrnpayment (
    payment_code        VARCHAR,
    payment_name        VARCHAR,
    PRIMARY KEY (payment_code)
);

CREATE TABLE IF NOT EXISTS esrn.nominated_payment (
    id                              SERIAL,
    esrn_payment_payment_code       VARCHAR NOT NULL ,
    card_holder_pers_id             INTEGER NOT NULL ,
    open_date                       DATE ,
    close_date                      DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (esrn_payment_payment_code) REFERENCES esrn.esrnpayment(payment_code),
    FOREIGN KEY (card_holder_pers_id) REFERENCES main.holders(pers_id)
);

