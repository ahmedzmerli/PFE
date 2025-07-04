export interface PdvMaster {
  id: number;   // L’identifiant unique du PDV
  msisdn: string;        // Le MSISDN principal (ex: premier numéro associé)
  nomPdv: string;
  adresse: string;
  codePdv: string;
}
