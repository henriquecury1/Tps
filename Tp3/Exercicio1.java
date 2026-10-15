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

class Exercicio1{
    private Veiculo[] array;

    public Exercicio1(Veiculo[] array){
        this.array = array;
    }

    //A ideia aqui é dividir o vetor, ordenar recursivamente e juntar as partes ordenadas
    public void mergesort(int esq, int dir){
        if(esq < dir){
            int meio = (esq + dir)/2;
            mergesort(esq, meio);
            mergesort(meio + 1, dir);
            intercalar(esq, meio, dir);
        }
    }

    public void intercalar(int esq, int meio, int dir){
        //O pedaço esq vai até meio e o direito de meio+1 até dir
        int nEsq = (meio + 1) - esq;
        int nDir = dir - meio;

        //Faz a cópia temporária dos dois pedaços
        Veiculo[] arrayEsq = new Veiculo[nEsq + 1];
        Veiculo[] arrayDir = new Veiculo[nDir + 1];

        arrayEsq[nEsq] = null;
        arrayDir[nDir] = null;

        int iEsq, iDir, i;

        //Faz a cópia dos arrays
        for(iEsq = 0; iEsq < nEsq; iEsq++){
            arrayEsq[iEsq] = array[esq + iEsq];
        }
        for(iDir = 0; iDir < nDir; iDir++){
            arrayDir[iDir] = array[(meio+1)+iDir];
        }

        //Realmente intercala
        for(iEsq = iDir = 0, i = esq; i <= dir; i++){
            if(arrayEsq[iEsq] == null){
                array[i] = arrayDir[iDir++];
            } else if(arrayDir[iDir] == null){
                array[i] = arrayEsq[iEsq++];
            } else if(comparar(arrayEsq[iEsq], arrayDir[iDir]) <= 0){
                array[i] = arrayEsq[iEsq++];
            } else {
                array[i] = arrayDir[iDir++];
            }
        }
    }

    // consumo_cidade é a chave; em empate, desempata por categoria
    private int comparar(Veiculo a, Veiculo b){
        int cmp = Float.compare(a.getCidade(), b.getCidade());
        if(cmp != 0){
            return cmp;
        }
        return a.getCategoria().compareTo(b.getCategoria());
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        LeitorCsv leitor = new LeitorCsv();
        Veiculo[] veiculos = leitor.ler("/tmp/veiculos.csv");

        Veiculo[] selecionados = new Veiculo[1000];
        int n = 0;

        while(sc.hasNextInt()){
            int id = sc.nextInt();
            if(id == -1){
                break;
            }
            selecionados[n] = buscarPorId(veiculos, id);
            n++;
        }
        sc.close();

        Veiculo[] array = new Veiculo[n];
        for(int i = 0; i < n; i++){
            array[i] = selecionados[i];
        }

        Exercicio1 exercicio = new Exercicio1(array);
        exercicio.mergesort(0, n - 1);

        for(int i = 0; i < n; i++){
            imprimir(array[i]);
        }
    }

    private static void imprimir(Veiculo v){
        System.out.printf("[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %b ## %s]%n",
            v.getId(), v.getMarca(), v.getModelo(), v.getAno(), v.getCategoria(),
            String.join(",", v.getCombustivel()), v.getCilindros(), v.getCilindrada(),
            v.getTransmissao(), v.getTracao(), v.getCidade(), v.getEstrada(), v.getCo2(),
            v.getTurbo(), v.getData().format());
    }

    private static Veiculo buscarPorId(Veiculo[] veiculos, int id){
        for(int i = 0; i < veiculos.length && veiculos[i] != null; i++){
            if(veiculos[i].getId() == id){
                return veiculos[i];
            }
        }
        return null;
    }
}
