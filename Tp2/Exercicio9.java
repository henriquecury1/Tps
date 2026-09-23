import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Veiculo{
	private int id;
	private String marca;
	private String modelo;
	private int ano;
	private String categoria;
	private String[] combustivel;
	private int cilindros;
	private float cilindrada;
	private String transmissao;
	private String tracao;
	private float consumo_cidade;
	private float consumo_estrada;
	private float co2;
	private boolean turbo;
	private Data data_registro;

	public Veiculo(int id, String marca, String modelo, int ano, String categoria,
			String[] combustivel, int cilindros, float cilindrada, String transmissao,
			String tracao, float consumo_cidade, float consumo_estrada, float co2, boolean turbo, Data data_registro){
		this.id = id;
		this.marca = marca;
		this.modelo = modelo;
		this.ano = ano;
		this.categoria = categoria;
		this.combustivel = combustivel;
		this.cilindros = cilindros;
		this.cilindrada = cilindrada;
		this.transmissao = transmissao;
		this.tracao = tracao;
		this.consumo_cidade = consumo_cidade;
		this.consumo_estrada = consumo_estrada;
		this.co2 = co2;
		this.turbo = turbo;
		this.data_registro = data_registro;
	}

	public int getId(){
		return id;
	}
	public String getMarca(){
		return marca;
	}
	public String getModelo(){
		return modelo;
	}
	public int getAno(){
		return ano;
	}
	public String getCategoria(){
		return categoria;
	}
	public String[] getCombustivel(){
		return combustivel;
	}
	public int getCilindros(){
		return cilindros;
	}
	public float getCilindrada(){
		return cilindrada;
	}
	public String getTransmissao(){
		return transmissao;
	}
	public String getTracao(){
		return tracao;
	}
	public float getCidade(){
		return consumo_cidade;
	}
	public float getEstrada(){
		return consumo_estrada;
	}
	public float getCo2(){
		return co2;
	}
	public boolean getTurbo(){
		return turbo;
	}
	public Data getData(){
		return data_registro;
	}

    public static Veiculo parseVeiculo(String s){
        String[] campos = s.split(",");
        int id = Integer.parseInt(campos[0]);
        String marca = campos[1];
        String modelo = campos[2];
        int ano = Integer.parseInt(campos[3]);
        String categoria = campos[4];
        String[] combustivel = campos[5].split(";");
        int cilindros = Integer.parseInt(campos[6]);
        float cilindrada = Float.parseFloat(campos[7]);
        String transmissao = campos[8];
        String tracao = campos[9];
        float consumo_cidade = Float.parseFloat(campos[10]);
        float consumo_estrada = Float.parseFloat(campos[11]);
        float co2 = Float.parseFloat(campos[12]);
        boolean turbo = Boolean.parseBoolean(campos[13]);
        Data data_registro = Data.parseData(campos[14]);
        Veiculo v = new Veiculo(id, marca, modelo, ano, categoria, combustivel, cilindros, cilindrada, transmissao, 
			                    tracao, consumo_cidade, consumo_estrada, co2, turbo, data_registro);
        return v;
    }

    public String format(){
        return String.format("%d,%s,%s,%d,%s,%s,%d,%.1f,%s,%s,%.2f,%.2f,%.2f,%b,%s", id, marca, modelo, ano, categoria,
		                     String.join(";", combustivel), cilindros, cilindrada, transmissao, tracao, consumo_cidade, 
							 consumo_estrada, co2, turbo, data_registro.format());
    }

}

class Data{
    private int dia;
    private int mes;
    private int ano;

    public Data(int d, int m, int a){
        this.dia = d;
        this.mes = m;
        this.ano = a;
    }

    public int getDia(){
        return dia;
    }
    public int getMes(){
        return mes;
    }
    public int getAno(){
        return ano;
    }

    public static Data parseData(String s){
        String[] campos = s.split("-");
        int ano = Integer.parseInt(campos[0]);
        int mes = Integer.parseInt(campos[1]);
        int dia = Integer.parseInt(campos[2]);
        return new Data(dia, mes, ano);
    }

    public String format(){
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class LeitorCsv {
    private Veiculo[] arrayVeiculo;

    public Veiculo[] ler(String caminho) {
        arrayVeiculo = new Veiculo[1000];
        int i = 0;

        try {
            Scanner sc = new Scanner(new File(caminho));

            while (sc.hasNext()) {
                String linha = sc.nextLine();
                arrayVeiculo[i] = Veiculo.parseVeiculo(linha);
                i++;
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
        }

        return arrayVeiculo;
    }
}

class Lista{
    private Veiculo[] vet;
    private int n;

    public Lista(){
        this(1000);
    }

    Lista(int tamanho){
        vet = new Veiculo[tamanho];
        n = 0;
    }

    public void inserirInicio(Veiculo veiculo) throws Exception{
        if(n >= vet.length)
            throw new Exception("Erro");

        for(int i = n; i > 0; i--){
            vet[i] = vet[i - 1];
        }

        vet[0] = veiculo;
        n++;
    }

    public void inserir(Veiculo veiculo, int pos) throws Exception{
        if(n >= vet.length || pos < 0 || pos > n)
            throw new Exception("Erro");

        for(int i = n; i > pos; i--){
            vet[i] = vet[i - 1];
        }

        vet[pos] = veiculo;
        n++;
    }

    public void inserirFim(Veiculo veiculo) throws Exception{
        if(n >= vet.length)
            throw new Exception("Erro");

        vet[n] = veiculo;
        n++;
    }

    public Veiculo removerInicio() throws Exception{
        if(n == 0)
            throw new Exception("Erro");

        Veiculo tmp = vet[0];
        n--;

        for(int i = 0; i < n; i++){
            vet[i] = vet[i + 1];
        }
        return tmp;
    }

    public Veiculo removerFim() throws Exception{
        if(n == 0)
            throw new Exception("Erro");

        Veiculo tmp = vet[n - 1];
        n--;

        return tmp;
    }

    public Veiculo remover(int pos) throws Exception{
        if(n == 0 || pos < 0 || pos >= n)
            throw new Exception("Erro");

        Veiculo tmp = vet[pos];
        n--;

        for(int i = pos; i < n; i++){
            vet[i] = vet[i + 1];
        }

        return resp;
    }

    public void mostrar(){
        for(int i = 0; i < n; i++){
            System.out.println(vet[i].format());
        }
    }
}

class Exercicio9{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        LeitorCsv leitor = new LeitorCsv();
        Veiculo[] veiculos = leitor.ler("/tmp/veiculos.csv");

        Lista lista = new Lista();

        

    }
}
