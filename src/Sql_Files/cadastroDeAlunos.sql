CREATE DATABASE cadastroDeAlunos;
USE cadastroDeAlunos;

-- Tabela de cursos da instituição
CREATE TABLE IF NOT EXISTS cursos(
	cursos_id INT PRIMARY KEY AUTO_INCREMENT,
	nomes VARCHAR(50) NOT NULL,
    coordenadores VARCHAR(50) NOT NULL
);

-- Tabela de disciplinas dos cursos
CREATE TABLE IF NOT EXISTS disciplinas(
	disciplinas_id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    professor VARCHAR(100) NOT NULL,
    situacao VARCHAR(100) NOT NULL,
    cursos_id INT NOT NULL,
    FOREIGN KEY(cursos_id) REFERENCES cursos(cursos_id)
);

-- Matrículas de cada aluno
CREATE TABLE IF NOT EXISTS matriculas(
	registro INT PRIMARY KEY AUTO_INCREMENT,
	ativa BINARY (1) NOT NULL
);

-- Informações de cada aluno
CREATE TABLE IF NOT EXISTS alunos(
	ra VARCHAR(10) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    curso VARCHAR(50) NOT NULL,
	matriculas_id INT NOT NULL,
	FOREIGN KEY(matriculas_id) REFERENCES matriculas(registro)
);

-- Quais alunos estão em cada disciplina e suas respectivas relações
CREATE TABLE IF NOT EXISTS disciplinas_alunos(
	diciplinas_alunos_id INT PRIMARY KEY AUTO_INCREMENT,
	disciplinas_id INT NOT NULL,
    alunos_ra VARCHAR(10) NOT NULL,
    notas DECIMAL(10,2) NOT NULL,
    FOREIGN KEY(disciplinas_id) REFERENCES disciplinas(disciplinas_id),
    FOREIGN KEY(alunos_ra) REFERENCES alunos(ra)
);

-- Turmas que cada disciplina possui
CREATE TABLE IF NOT EXISTS turmas (
	idTurma INT PRIMARY KEY AUTO_INCREMENT,
	ra VARCHAR(10) NOT NULL,
	disciplinas_id INT NOT NULL,
    FOREIGN KEY (disciplinas_id) REFERENCES disciplinas (disciplinas_id)
);
