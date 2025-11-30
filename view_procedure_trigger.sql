DROP VIEW IF EXISTS vw_casos_ativos_detalhes;
DROP PROCEDURE IF EXISTS encerrar_caso;
DROP TRIGGER IF EXISTS tr_check_pessoa_existente;

DELIMITER $$
  
CREATE PROCEDURE encerrar_caso (
    IN p_id_caso INT,
    IN p_conteudo_relatorio TEXT
)
BEGIN
    DECLARE v_status_atual VARCHAR(30);
    DECLARE v_policial_responsavel INT;

    START TRANSACTION;

    SELECT 
        status_caso, 
        policial_responsavel
    INTO 
        v_status_atual, 
        v_policial_responsavel
    FROM 
        caso
    WHERE 
        id_caso = p_id_caso;

    IF v_status_atual IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Erro: Caso não encontrado.';
        ROLLBACK;
    
    ELSEIF v_status_atual = 'Encerrado' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Erro: O caso já está encerrado.';
        ROLLBACK;

    ELSE
        UPDATE 
            caso
        SET 
            status_caso = 'Encerrado',
            data_fechamento = CURDATE()
        WHERE 
            id_caso = p_id_caso;

        INSERT INTO relatorio (
            id_caso, 
            conteudo, 
            policial_emissor
        ) VALUES (
            p_id_caso, 
            CONCAT('RELATÓRIO FINAL DE ENCERRAMENTO: ', p_conteudo_relatorio), 
            v_policial_responsavel
        );
        
        COMMIT;

    END IF;
END$$

CREATE TRIGGER tr_check_pessoa_existente
BEFORE INSERT ON ocorrencia
FOR EACH ROW
BEGIN
    DECLARE v_count INT;

    SELECT 
        COUNT(*) 
    INTO 
        v_count
    FROM 
        pessoa
    WHERE 
        cpf = NEW.cpf_declarante;

    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Erro: O CPF do declarante não está cadastrado na tabela Pessoa.';
    END IF;
END$$

DELIMITER ;

CREATE VIEW vw_casos_ativos_detalhes AS
SELECT
    c.id_caso AS ID_Caso,
    c.status_caso AS Status,
    c.data_abertura AS Data_Abertura,
    
    o.num_boletim AS Num_Boletim,
    o.data_hora_registro AS Data_Hora_Ocorrencia,
    o.descricao AS Descricao_Ocorrencia,
    p_declarante.nome AS Nome_Declarante,
    p_declarante.cpf AS CPF_Declarante,
    
    p_responsavel.num_distintivo AS Distintivo_Policial,
    p_responsavel.cargo AS Cargo_Policial,
    p_policial.nome AS Nome_Policial_Responsavel

FROM
    caso c
INNER JOIN 
    ocorrencia o ON c.num_boletim = o.num_boletim
INNER JOIN 
    policial p_responsavel ON c.policial_responsavel = p_responsavel.num_distintivo
INNER JOIN 
    pessoa p_policial ON p_responsavel.cpf = p_policial.cpf
INNER JOIN
    pessoa p_declarante 
    ON o.cpf_declarante = p_declarante.cpf 

WHERE
    c.status_caso NOT IN ('Encerrado', 'Arquivado')
ORDER BY
    c.data_abertura DESC;
