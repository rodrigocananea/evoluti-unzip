# EvoluTI UnZip

Para obter os detalhes execute o seguinte comando:
```sh
~/ java -jar UnZip.jar -a
```

Aplicativo UnZip.jar
Abaixo segue uma lista com os possveis parametros para serem utilizados:

| Parametro 	| Descrição | Exemplo | Obrigatório |
|---------------|-----------|---------|--------------|
| -a  <br/> --ajuda  | Lista parametros para ajuda. 	 					  | java -jar UnZip -a | Não |
| -r            | Se existir esse parametro ele deleta o arquivo depois de descompactado. |java -jar UnZip -zip="C:\MinhaPasta\MeuArquivo.zip" -r | Não |
| -zip=         | Informar aonde est o arquivo para ser descompactado | java -jar UnZip -zip="C:\MinhaPasta\MeuArquivo.zip" 	| Sim |
| -saida=       | Informar aonde deseja descompactar o arquivo | java -jar UnZip  -zip="C:\MinhaPasta\MeuArquivo.zip" -saida="C:\MinhaPasta\NestaPasta\" | Não |

