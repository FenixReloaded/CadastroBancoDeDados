USE cadastroDeAlunos;

-- Questão 1: Saber quem são os alunos da Faculdade
SELECT * FROM alunos
ORDER BY matriculas_id;

-- Questão 2: Saber quais alunos estão matriculados em determinada disciplina
SELECT a.nome AS nome_do_aluno, 
       d.nome AS nome_da_disciplina
FROM disciplinas_alunos da
INNER JOIN alunos a ON a.ra = da.alunos_ra
INNER JOIN disciplinas d ON d.disciplinas_id = da.disciplinas_id
WHERE da.disciplinas_id = 1  -- Para saber sobre as demais disciplinas, apenas troque o valor do número (conferir tabela)
ORDER BY a.nome;

-- Questão 3: Saber quantos alunos tem em cada curso
SELECT curso, COUNT(*) AS total_alunos_do_curso 
FROM alunos
GROUP BY curso;

-- Questão 4: Saber se tem alguma turma* com menos que 5 alunos
SELECT d.nome AS nome_da_disciplina, COUNT(*) AS quantidade_de_alunos 
FROM disciplinas d
INNER JOIN turmas t ON d.disciplinas_id = t.disciplinas_id
GROUP BY d.nome
HAVING COUNT(*) < 5; 

-- Questão 5: Saber quais alunos não realizaram a matricula 
SELECT * FROM alunos
INNER JOIN matriculas m ON m.registro = alunos.matriculas_id
WHERE m.ativa = 1; -- 0 para matrícula não ativa e 1 para matrícula ativa

-- Questão 6: Saber quais alunos foram aprovados em determinada disciplina
SELECT * FROM alunos
INNER JOIN matriculas m ON m.registro = alunos.matriculas_id
INNER JOIN disciplinas_alunos d ON d.alunos_ra = alunos.ra
WHERE d.disciplinas_id = '1' AND d.notas > 5; -- Para saber sobre as demais disciplinas, apenas troque o valor do número (conferir tabela)