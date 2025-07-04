export interface Pdv {
  msisdn: string;
  idPdvMaster?: number; // optionnel car pas toujours présent lors de la création
  nomPdv: string;
  adresse: string;
  codePdv: string;
}
