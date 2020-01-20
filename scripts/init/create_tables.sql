
--create transaction table
create table transaction (
    transaction_id BIGINT unsigned not null auto_increment,
    account_number varchar(255) not null,
    balance decimal(19,2) not null,
    check_column varchar(255) not null,
    credit decimal(19,2),
    debit decimal(19,2),
    description varchar(255) not null,
    post_date date not null,
    status varchar(255) not null,
    primary key (transaction_id)
) engine=InnoDB;

--create transaction_tag table
create table transaction_tag (
    transaction_tag_id BIGINT unsigned not null auto_increment,
    name varchar(255),
    primary key (transaction_tag_id)
) engine=InnoDB;

--create transaction_matcher table
create table transaction_matcher (
    transaction_matcher_id BIGINT unsigned not null auto_increment,
    match_string varchar(255),
    name varchar(255),
    transaction_tag_id BIGINT unsigned,
    transaction_tag_group_id BIGINT unsigned,
    primary key (transaction_matcher_id)
) engine=InnoDB;

--create transaction_tag_group table
create table transaction_tag_group (
    transaction_tag_group_id BIGINT unsigned not null auto_increment,
    name varchar(255),
    primary key (transaction_tag_group_id)
) engine=InnoDB;

--create transaction_transaction_tag associative table
create table transaction_transaction_tag (
    transaction_transaction_tag_id BIGINT unsigned not null auto_increment,
    transaction_id BIGINT unsigned not null,
    transaction_tag_id BIGINT unsigned not null,
    primary key (transaction_transaction_tag_id)
) engine=InnoDB;

--create transaction_tag_group_transaction_tag associative table
create table transaction_tag_group_transaction_tag (
    transaction_tag_group_transaction_tag_id BIGINT unsigned not null auto_increment,
    transaction_tag_group_id BIGINT unsigned not null,
    transaction_tag_id BIGINT unsigned not null,
    primary key (transaction_tag_group_transaction_tag_id)
) engine=InnoDB;

--add Foreign Key Constraints on transaction_transaction_tag associative table
alter table transaction_transaction_tag
    add constraint FK_TTAG_TRANSACTION_ID
    foreign key (transaction_id)
    references transaction (transaction_id);

alter table transaction_transaction_tag
    add constraint FK_TTAG_TRANSACTION_TAG_ID
    foreign key (transaction_tag_id)
    references transaction_tag (transaction_tag_id);

--add Foreign Key Constraints on transaction_matcher table
alter table transaction_matcher
    add constraint FK_TM_TRANSACTION_TAG_ID
    foreign key (transaction_tag_id)
    references transaction_tag (transaction_tag_id);

alter table transaction_matcher
    add constraint FK_TM_TRANSACTION_TAG_GROUP_ID
    foreign key (transaction_tag_group_id)
    references transaction_tag_group (transaction_tag_group_id);

--add Foreign Key Constraints on transaction_tag_group_transaction_tag associative table
alter table transaction_tag_group_transaction_tag
    add constraint FK_TTGTT_TRANSACTION_TAG_ID
    foreign key (transaction_tag_id)
    references transaction_tag (transaction_tag_id);

alter table transaction_tag_group_transaction_tag
    add constraint FK_TTGTT_TRANSACTION_TAG_GROUP_ID
    foreign key (transaction_tag_group_id)
    references transaction_tag_group (transaction_tag_group_id);
