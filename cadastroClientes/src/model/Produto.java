package model;

public class Produto {

    private int id; // Novo campo
    private String referencia;
    private String produto;
    private String superficie;
    private String colecao;
    private String formato;
    private String localUso;
    private double m2Pallet;
    private double kgM2;
    private double m2Caixa;
    private int pcsCaixa;
    private double valorPalletizado;

    // Construtor com ID
    public Produto(int id, String referencia, String produto, String superficie, String colecao, String formato,
                   String localUso, double m2Pallet, double kgM2, double m2Caixa, int pcsCaixa,
                   double valorPalletizado) {
        this.id = id;
        this.referencia = referencia;
        this.produto = produto;
        this.superficie = superficie;
        this.colecao = colecao;
        this.formato = formato;
        this.localUso = localUso;
        this.m2Pallet = m2Pallet;
        this.kgM2 = kgM2;
        this.m2Caixa = m2Caixa;
        this.pcsCaixa = pcsCaixa;
        this.valorPalletizado = valorPalletizado;
    }

    // Construtor sem ID (já existente)
    public Produto(String referencia, String produto, String superficie, String colecao, String formato,
                   String localUso, double m2Pallet, double kgM2, double m2Caixa, int pcsCaixa,
                   double valorPalletizado) {
        this.referencia = referencia;
        this.produto = produto;
        this.superficie = superficie;
        this.colecao = colecao;
        this.formato = formato;
        this.localUso = localUso;
        this.m2Pallet = m2Pallet;
        this.kgM2 = kgM2;
        this.m2Caixa = m2Caixa;
        this.pcsCaixa = pcsCaixa;
        this.valorPalletizado = valorPalletizado;
    }

    // Getter e Setter do ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Demais getters e setters (já existentes)

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public String getColecao() {
        return colecao;
    }

    public void setColecao(String colecao) {
        this.colecao = colecao;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getLocalUso() {
        return localUso;
    }

    public void setLocalUso(String localUso) {
        this.localUso = localUso;
    }

    public double getM2Pallet() {
        return m2Pallet;
    }

    public void setM2Pallet(double m2Pallet) {
        this.m2Pallet = m2Pallet;
    }

    public double getKgM2() {
        return kgM2;
    }

    public void setKgM2(double kgM2) {
        this.kgM2 = kgM2;
    }

    public double getM2Caixa() {
        return m2Caixa;
    }

    public void setM2Caixa(double m2Caixa) {
        this.m2Caixa = m2Caixa;
    }

    public int getPcsCaixa() {
        return pcsCaixa;
    }

    public void setPcsCaixa(int pcsCaixa) {
        this.pcsCaixa = pcsCaixa;
    }

    public double getValorPalletizado() {
        return valorPalletizado;
    }

    public void setValorPalletizado(double valorPalletizado) {
        this.valorPalletizado = valorPalletizado;
    }
}
