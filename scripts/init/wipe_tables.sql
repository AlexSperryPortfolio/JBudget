
delete from transaction_transaction_tag where transaction_transaction_tag_id > 0;
delete from transaction_tag where transaction_tag_id > 0;
delete from transaction where transaction_id > 0;

alter table transaction_transaction_tag AUTO_INCREMENT = 1;
alter table transaction_tag AUTO_INCREMENT = 1;
alter table transaction AUTO_INCREMENT = 1;

commit;