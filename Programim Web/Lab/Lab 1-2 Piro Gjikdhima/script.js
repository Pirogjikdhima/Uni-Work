const emri = document.getElementById("emri");
const zgjedhje = document.getElementsByName("zgjedhje");
const letrat = document.getElementById("letrat");
const ngjyrabox = document.getElementById("ngjyrabox");
const dedikimbox = document.getElementById("dedikimbox");
const dedikim = document.getElementById("dedikim");
const totali = document.getElementById("totali");
const fshi = document.getElementById("fshi");
const llogari = document.getElementById("llogari");



let eshteLlogaritur = false;
let isButtonclickedCorrectly = false;
dedikim.disabled = true;


dedikimbox.addEventListener("change", () => {
    if(dedikimbox.checked){
        dedikim.disabled = false;
    }
    else{
        dedikim.disabled = true;
        dedikim.value = "";
    }
});


document.querySelectorAll("input , select").forEach(element => {
    element.addEventListener("change", () => {
        if (eshteLlogaritur) {
            llogaritTotalin();
        }
    });
});



llogari.addEventListener("click",() => {
    let kusht = true;
    
    document.querySelectorAll(".error").forEach(element => {
        if (element.textContent.trim() !== "") {
            kusht = false ;
        }
    });

    if(kusht) eshteLlogaritur = true;
    llogaritTotalin();
});

fshi.addEventListener("click", () => {
    document.getElementById("lab-form").reset();
    totali.value="";
    dedikim.disabled = true;
    eshteLlogaritur = isButtonclickedCorrectly =false;
    document.querySelectorAll(".error").forEach(element => {
        element.textContent = "";
    });
    
});


function llogaritTotalin(){
    let shuma = 0;
    let valid = true

    document.querySelectorAll(".error").forEach(element => {
        element.textContent = "";
    });

    if(emri.value.trim() === ""){
        valid = false
        let emriError = document.getElementById("emriError");
        emriError.textContent = "Ju lutem vendosni emrin";
    }
    
    let zgj = Array.from(zgjedhje).find(radio => radio.checked);
    if(!zgj){
        valid = false
        let tipiError = document.getElementById("tipiError");
        tipiError.textContent = "Ju lutem zgjedhni tipin e lidhjes";
    }else{
        shuma += parseInt(zgj.value);
    }

    if(!letrat.value){
        valid = false

        document.getElementById("opsioneError").textContent = "Ju lutem zgjedhni tipin e letres";
    }else{
        shuma += parseInt(letrat.value);
    }
    if(ngjyrabox.checked) shuma += parseInt(ngjyrabox.value);
    if(dedikimbox.checked)shuma += parseInt(dedikimbox.value);

    if(valid){
        totali.value = shuma;
    }else{
        totali.value = "";
    }
}