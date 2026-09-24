#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
 
#define TAM_STR 50
#define MAX_COMBUSTIVEIS 5
#define TAM_STR_COMBUSTIVEL 30
#define MAX_VEICULOS 1000
 
typedef struct {
    int ano;
    int mes;
    int dia;
} Data;
 
typedef struct {
    int id;
    char marca[TAM_STR];
    char modelo[TAM_STR];
    int ano;
    char categoria[TAM_STR];
    char combustivel[MAX_COMBUSTIVEIS][TAM_STR_COMBUSTIVEL];
    int cilindros;
    double cilindrada;
    char transmissao[TAM_STR];
    char tracao[TAM_STR];
    double consumoCidade;
    double consumoEstrada;
    double co2;
    bool turbo;
    Data dataRegistro;
} Veiculo;
 
Data parseData(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}
 
void formatData(Data d, char* buffer) {
    sprintf(buffer, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
}
 
static void removerQuebraDeLinha(char* s) {
    int i = 0;
    while (s[i] != '\0') {
        i++;
    }
    while (i > 0 && (s[i - 1] == '\n' || s[i - 1] == '\r')) {
        i--;
    }
    s[i] = '\0';
}
 
static void parseCombustiveis(char* campo, Veiculo* v) {
    int i = 0;
    int j = 0;
    int k = 0;
 
    while (campo[k] != '\0' && i < MAX_COMBUSTIVEIS) {
        if (campo[k] == ';') {
            v->combustivel[i][j] = '\0';
            i++;
            j = 0;
        } else {
            v->combustivel[i][j] = campo[k];
            j++;
        }
        k++;
    }
    if (i < MAX_COMBUSTIVEIS) {
        v->combustivel[i][j] = '\0';
        i++;
    }
    for (; i < MAX_COMBUSTIVEIS; i++) {
        v->combustivel[i][0] = '\0';
    }
}
 
Veiculo* parseVeiculo(char* s) {
    Veiculo* v = (Veiculo*) malloc(sizeof(Veiculo));
    char copia[1024];
    sprintf(copia, "%s", s);
    removerQuebraDeLinha(copia);
    char* campo;
    campo = strtok(copia, ",");
    v->id = atoi(campo);
    campo = strtok(NULL, ",");
    sprintf(v->marca, "%s", campo);
    campo = strtok(NULL, ",");
    sprintf(v->modelo, "%s", campo);
    campo = strtok(NULL, ",");
    v->ano = atoi(campo);
    campo = strtok(NULL, ",");
    sprintf(v->categoria, "%s", campo);
    campo = strtok(NULL, ",");
    parseCombustiveis(campo, v);
    campo = strtok(NULL, ",");
    v->cilindros = atoi(campo);
    campo = strtok(NULL, ",");
    v->cilindrada = atof(campo);
    campo = strtok(NULL, ",");
    sprintf(v->transmissao, "%s", campo);
    campo = strtok(NULL, ",");
    sprintf(v->tracao, "%s", campo);
    campo = strtok(NULL, ",");
    v->consumoCidade = atof(campo);
    campo = strtok(NULL, ",");
    v->consumoEstrada = atof(campo);
    campo = strtok(NULL, ",");
    v->co2 = atof(campo);
    campo = strtok(NULL, ",");
    v->turbo = (strcmp(campo, "true") == 0);
    campo = strtok(NULL, ",");
    v->dataRegistro = parseData(campo);
 
    return v;
}
 
void formatVeiculo(Veiculo v, char* buffer) {
    char combustivelBuffer[200];
    int pos = 0;
    for (int i = 0; i < MAX_COMBUSTIVEIS; i++) {
        if (v.combustivel[i][0] == '\0') {
            break;
        }
        if (i > 0) {
            pos += sprintf(combustivelBuffer + pos, ";");
        }
        pos += sprintf(combustivelBuffer + pos, "%s", v.combustivel[i]);
    }
    combustivelBuffer[pos] = '\0';
 
    char dataBuffer[20];
    formatData(v.dataRegistro, dataBuffer);
 
    sprintf(buffer, "%d,%s,%s,%d,%s,%s,%d,%.1f,%s,%s,%.2f,%.2f,%.2f,%s,%s",
            v.id, v.marca, v.modelo, v.ano, v.categoria, combustivelBuffer,
            v.cilindros, v.cilindrada, v.transmissao, v.tracao,
            v.consumoCidade, v.consumoEstrada, v.co2,
            v.turbo ? "true" : "false", dataBuffer);
}
 
Veiculo* lerCsv(char* caminhoArquivo, int* n) {
    Veiculo* veiculos = (Veiculo*) malloc(MAX_VEICULOS * sizeof(Veiculo));
    *n = 0;
 
    FILE* arquivo = fopen(caminhoArquivo, "r");
    if (arquivo == NULL) {
        printf("Arquivo nao encontrado: %s\n", caminhoArquivo);
        return veiculos;
    }
 
    char linha[1024];
    while (fgets(linha, sizeof(linha), arquivo) != NULL && *n < MAX_VEICULOS) {
        Veiculo* v = parseVeiculo(linha);
        veiculos[*n] = *v;
        free(v);
        (*n)++;
    }
 
    fclose(arquivo);
    return veiculos;
}
