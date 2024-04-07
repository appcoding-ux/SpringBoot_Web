async function uploadToServer(formObj){
    console.log(formObj);

    const response = await axios({
        method : 'post',
        url : '/upload',
        data : formObj,
        headers : {
            'Content-Type' : 'multipart/form-data',
        },
    });

    return response.data;
}

async function removeFileToServer(uuid, fileName){
    const response = await axios.delete(`/remove/${uuid}_${fileName}`);

    return response.data;
}

function showUploadFile({uuid, fileName, link}){
    const uploadResult = document.querySelector('.uploadResult');

    const str = `<div class='card col-4'>
                        <div class='card-header d-flex justify-content-center'>${fileName}
                        <button class="btn-sm btn-danger" onclick="javascript:removeFile('${uuid}', '${fileName}', this)">X</button>
                        </div>
                        <div>
                            <img src="/view/${link}" data-src="${uuid+"_"+fileName}">
                        </div>
                        </div>`

    uploadResult.innerHTML += str;
}

function removeFile(uuid, fileName, obj){
    const targetDiv = obj.closest(".card")

    removeFileToServer(uuid, fileName).then(data => {
        targetDiv.remove();
    })
}