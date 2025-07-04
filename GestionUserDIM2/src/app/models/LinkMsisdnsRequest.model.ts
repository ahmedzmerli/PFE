export interface PdvLite {
  nomPdv: string;
  adresse?: string;
  codePdv?: string;
}

export interface LinkMsisdnsRequest {
  msisdns: string[];
  pdv: PdvLite;
}
