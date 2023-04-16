CREATE INDEX index_category ON tripbuilder2.tb_category USING gin (value);
CREATE INDEX index_move ON tripbuilder2.tb_move USING gin (value);
CREATE INDEX index_poi ON tripbuilder2.tb_poi USING gin (value);