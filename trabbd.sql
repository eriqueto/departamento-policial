DROP TABLE IF EXISTS RELATORIO;
DROP TABLE IF EXISTS DEPOIMENTO;
DROP TABLE IF EXISTS PESSOA_ENVOLVIDA;
DROP TABLE IF EXISTS EVIDENCIA;
DROP TABLE IF EXISTS CASO;
DROP TABLE IF EXISTS OCORRENCIA;
DROP TABLE IF EXISTS POLICIAL;
DROP TABLE IF EXISTS DELEGACIA;
DROP TABLE IF EXISTS PESSOA;
DROP TABLE IF EXISTS ENDERECO;


CREATE TABLE ENDERECO (
	id_endereco INT PRIMARY KEY AUTO_INCREMENT,
	logradouro VARCHAR(100),
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cep VARCHAR(10)
);

CREATE TABLE PESSOA (
	cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sexo VARCHAR(1),
    data_nascimento DATE,
    telefone VARCHAR(20),
    id_endereco INT,
    foto_perfil BLOB,
    
    FOREIGN KEY (id_endereco) REFERENCES ENDERECO(id_endereco)
);


CREATE TABLE DELEGACIA (
	id_delegacia INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    id_endereco INT,
    
    FOREIGN KEY (id_endereco) REFERENCES ENDERECO(id_endereco)
);

CREATE TABLE POLICIAL (
    num_distintivo INT PRIMARY KEY AUTO_INCREMENT,
    cpf VARCHAR(11) NOT NULL,
    cargo VARCHAR(50),
    id_delegacia INT,
    
    FOREIGN KEY (id_delegacia) REFERENCES DELEGACIA(id_delegacia),
    FOREIGN KEY (cpf) REFERENCES PESSOA(cpf),
    UNIQUE(cpf)
);

CREATE TABLE OCORRENCIA (
	num_boletim INT PRIMARY KEY AUTO_INCREMENT,
    data_hora_registro DATETIME,
    cpf_declarante VARCHAR(11) NOT NULL,
    policial_registro INT NOT NULL,
    descricao TEXT,
    FOREIGN KEY (policial_registro) REFERENCES POLICIAL(num_distintivo),
    FOREIGN KEY (cpf_declarante) REFERENCES PESSOA(cpf)
);

CREATE TABLE CASO (
	id_caso INT PRIMARY KEY AUTO_INCREMENT,
    num_boletim INT,
    status_caso VARCHAR(30),
    policial_responsavel INT,
    data_abertura DATE,
    data_fechamento DATE,
    
    FOREIGN KEY (num_boletim) REFERENCES OCORRENCIA(num_boletim),
    FOREIGN KEY (policial_responsavel) REFERENCES POLICIAL(num_distintivo)
);

CREATE TABLE EVIDENCIA (
	id_evidencia INT PRIMARY KEY AUTO_INCREMENT,
    id_caso INT,
    descricao TEXT,
    localizacao VARCHAR(150),
    data_coleta DATE,
    
    FOREIGN KEY (id_caso) REFERENCES CASO(id_caso)
);

CREATE TABLE PESSOA_ENVOLVIDA (
	id_caso INT NOT NULL,
    cpf_pessoa VARCHAR(11) NOT NULL,
    tipo_envolvimento VARCHAR(25),
    
    PRIMARY KEY (id_caso, cpf_pessoa),
    FOREIGN KEY (id_caso) REFERENCES CASO(id_caso),
    FOREIGN KEY (cpf_pessoa) REFERENCES PESSOA(cpf)
);

CREATE TABLE DEPOIMENTO (
	id_depoimento INT PRIMARY KEY AUTO_INCREMENT,
    id_caso INT NOT NULL,            
    cpf_pessoa VARCHAR(11) NOT NULL,     
    data_hora_depoimento DATETIME,
    conteudo_depoimento TEXT,       
    
    FOREIGN KEY (id_caso, cpf_pessoa) REFERENCES PESSOA_ENVOLVIDA(id_caso, cpf_pessoa)
);

CREATE TABLE RELATORIO (
	id_relatorio INT PRIMARY KEY AUTO_INCREMENT,
    id_caso INT,
    conteudo TEXT,
    policial_emissor INT,
    
    FOREIGN KEY (policial_emissor) REFERENCES POLICIAL(num_distintivo),
    FOREIGN KEY (id_caso) REFERENCES CASO(id_caso)
);
