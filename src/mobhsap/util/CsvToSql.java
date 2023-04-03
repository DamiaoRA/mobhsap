package mobhsap.util;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class CsvToSql {

  private static final String TRAJECTORY_ID = "trajectory_id";
  private static final String TRAJECTORY_VALUE = "trajectory_value";
  private static final String DELIMIT = ",";
  private static final String DELIMIT_TRAJECTORY = ";";
  private static final short MAX_LINE = 500;

  /**
   * Para gerar um arquivo SQL a partir dos dados de uma fonte CSV devemos observar que o arquivo deverá considerar o
   * padrão pré-definido, onde é constituído de duas colunas: a primeira denominada de trajectory_id que identifica a
   * trajetória, onde a mesma é um ponto da rota e a segunda coluna, denominada de trajectory_value, constitui o valor
   * da propriedade em questão a ser analisada, por exemplo, o valor da categoria, POI, preço, clima daquele ponto da
   * trajetória. Outro ponto importante é que os dados devem estar ordenados pelo período da trajetória, para que assim,
   * os dados fiquem corretos. Um exemplo de consulta:
   *
   * select
   *    tid as trajectory_id,
   *    poi_name as trajectory_value
   * from
   *    foursquare.data_checkin
   * order by
   *    tid, date_time;
   *
   * Para executar este CSV e assim gerar a rota completa necessita ser informado os parâmetros obrigatórios:
   * o caminho do arquivo CSV e o esquema do banco de dados. A tabela de destino é criada a partir do nome do arquivo,
   * por exemplo, se o nome do arquivo CSV for tb_poi.csv a tabela a ser criada será tb_poi e sendo gerado o arquivo SQL
   * com o mesmo nome e no mesmo caminho do arquivo, contendo: o comando DDL de criação da tabela e os comandos DML de
   * INSERT das trajetórias.
   *
   * Existe um terceiro parâmetro --no-sanitize para não aplicar a higienização dos dados na coluna valores.
   * Sendo útil em casos em que a informação necessite ficar exatamente como foi criada, a exemplo, um número de ponto
   * flutuante, se for higienizada irá remover o sinal decimal (. ou ,). Por padrão, os dados da coluna trajectory_value
   * será higienizado.
   *
   * Exemplo de execução:
   *
   * java CsvToSql scripts/foursquare/tb_poi.csv foursquare
   * ou
   * java CsvToSql scripts/foursquare/tb_price.csv foursquare --no-sanitize
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    boolean sanitize = true;

    if (args.length == 0) {
      System.out.println("Por favor, informe o caminho do arquivo CSV.");
    } else if (args.length < 2) {
      System.out.println("Por favor, informe o schema do banco de dados.");
    } else if (args.length == 3) {
      sanitize = !Objects.equals(args[2], "--no-sanitize");
    }

    String pathFile = args[0];
    String schema = args[1];

    csvToSql(pathFile, schema, sanitize);
  }

  private static void csvToSql(
    final String pathFile,
    final String schema,
    final boolean sanitizeValue
  ) throws IOException {
    File fileCsv = new File(pathFile);
    String pathFileSql = fileCsv.getAbsolutePath().replace(".csv", ".sql");
    File fileSql = new File(pathFileSql);
    String tableName = schema + "." + fileCsv.getName().replace(".csv", "");

    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileCsv));
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileSql));

    String createTableSql = String.format(
      "CREATE TABLE IF NOT EXISTS %s (%s varchar(30) UNIQUE NOT NULL, %s TEXT NOT NULL);\n",
      tableName,
      TRAJECTORY_ID,
      TRAJECTORY_VALUE
    );

    try {
      bufferedReader.readLine();
      bufferedWriter.write(createTableSql);
      bufferedWriter.flush();

      int countLine = 0;
      Properties current = getPointTrajectory(
        bufferedReader.readLine(),
        sanitizeValue
      );

      if (!current.isEmpty()) {
        String trajectoryId = current.getProperty(TRAJECTORY_ID);
        String trajectory = current.getProperty(TRAJECTORY_VALUE);

        Properties next = getPointTrajectory(
          bufferedReader.readLine(),
          sanitizeValue
        );

        while (!next.isEmpty()) {
          if (countLine > MAX_LINE) {
            bufferedWriter.flush();
            countLine = 0;
          }

          if (
            Objects.equals(
              current.getProperty(TRAJECTORY_ID),
              next.getProperty(TRAJECTORY_ID)
            )
          ) {
            trajectoryId = current.getProperty(TRAJECTORY_ID);
            trajectory +=
              DELIMIT_TRAJECTORY + next.getProperty(TRAJECTORY_VALUE);
            current = (Properties) next.clone();
          } else {
            bufferedWriter.write(
              createInsertSql(tableName, trajectoryId, trajectory)
            );

            current = (Properties) next.clone();
            trajectoryId = current.getProperty(TRAJECTORY_ID);
            trajectory = current.getProperty(TRAJECTORY_VALUE);
          }

          next = getPointTrajectory(bufferedReader.readLine(), sanitizeValue);
          countLine++;
        }

        bufferedWriter.write(
          createInsertSql(tableName, trajectoryId, trajectory)
        );
      }
    } finally {
      bufferedReader.close();
      bufferedWriter.close();
    }
  }

  private static Properties getPointTrajectory(String line, boolean sanitize) {
    Properties pointTrajectory = new Properties();

    if (line != null) {
      String[] point = line.split(DELIMIT);
      pointTrajectory.setProperty(
        TRAJECTORY_ID,
        StringUtils.sanitize(point[0])
      );
      pointTrajectory.setProperty(
        TRAJECTORY_VALUE,
        sanitize ? StringUtils.sanitize(point[1]) : point[1]
      );
    }

    return pointTrajectory;
  }

  private static String createInsertSql(
    String tableName,
    String trajectoryId,
    String trajectory
  ) {
    String insertSql = "INSERT INTO %s VALUES ('%s', '%s');\n";

    return String.format(insertSql, tableName, trajectoryId, trajectory);
  }
}
