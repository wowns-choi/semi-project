// 이미지 미리보기 
document.querySelector('#d1').addEventListener('change', function(e){
	let img1 = this.previousElementSibling.previousElementSibling;
	
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		img1.src = e.target.result;
		img1.style.display = 'block';
	}
	
});
document.querySelector('#d2').addEventListener('change', function(e){
	let img1 = this.previousElementSibling.previousElementSibling;
	
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		img1.src = e.target.result;
		img1.style.display = 'block';
	}
	
});
document.querySelector('#d3').addEventListener('change', function(e){
	let img1 = this.previousElementSibling.previousElementSibling;
	
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		img1.src = e.target.result;
		img1.style.display = 'block';
	}
	
});
document.querySelector('#d4').addEventListener('change', function(e){
	let img1 = this.previousElementSibling.previousElementSibling;
	
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		img1.src = e.target.result;
		img1.style.display = 'block';
	}
	
});
document.querySelector('#d5').addEventListener('change', function(e){
	let img1 = this.previousElementSibling.previousElementSibling;
	
	let file = e.target.files[0];
	let reader = new FileReader();
	reader.readAsDataURL(file);
	
	reader.onload = function(e){
		img1.src = e.target.result;
		img1.style.display = 'block';
	}
	
});

// x 버튼 누를 시 
document.querySelector('#f1').addEventListener('click', function(e){
	
	let img1 = document.querySelector('#img1');
	// input 태그 갈아낀다. 
	document.querySelector('#d1').remove();	
	let newInput1 = document.createElement('input');
	newInput1.type = 'file';
	newInput1.id = 'd1';
	newInput1.name = 'main';
	newInput1.style.display = 'none';
		// 이벤트리스너 달아준다.	
	newInput1.addEventListener('change', function(e){
			let file = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file);
	
			reader.onload = function(e){
			img1.src = e.target.result;
			img1.style.display = 'block';
			}
	
	});
		
	document.querySelector('#register-image-div-1').append(newInput1);

	// 이미지 감춘다. 
	img1.style.display = 'none';
	
});
document.querySelector('#f2').addEventListener('click', function(e){
	
	let img2 = document.querySelector('#img2');
	// input 태그 갈아낀다. 
	document.querySelector('#d2').remove();	
	let newInput2 = document.createElement('input');
	newInput2.type = 'file';
	newInput2.id = 'd2';
	newInput2.name = 'sub1';
	newInput2.style.display = 'none';
		// 이벤트리스너 달아준다.	
	newInput2.addEventListener('change', function(e){
			let file = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file);
	
			reader.onload = function(e){
			img2.src = e.target.result;
			img2.style.display = 'block';
			}
	
	});
		
	document.querySelector('#register-image-div-2').append(newInput2);

	// 이미지 감춘다. 
	img2.style.display = 'none';
	
});
document.querySelector('#f3').addEventListener('click', function(e){
	
	let img3 = document.querySelector('#img3');
	// input 태그 갈아낀다. 
	document.querySelector('#d3').remove();	
	let newInput3 = document.createElement('input');
	newInput3.type = 'file';
	newInput3.id = 'd3';
	newInput3.name = 'sub2';
	newInput3.style.display = 'none';
		// 이벤트리스너 달아준다.	
	newInput3.addEventListener('change', function(e){
			let file = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file);
	
			reader.onload = function(e){
			img3.src = e.target.result;
			img3.style.display = 'block';
			}
	
	});
		
	document.querySelector('#register-image-div-3').append(newInput3);

	// 이미지 감춘다. 
	img3.style.display = 'none';
	
});
document.querySelector('#f4').addEventListener('click', function(e){
	
	let img4 = document.querySelector('#img4');
	// input 태그 갈아낀다. 
	document.querySelector('#d4').remove();	
	let newInput4 = document.createElement('input');
	newInput4.type = 'file';
	newInput4.id = 'd4';
	newInput4.name = 'sub3';
	newInput4.style.display = 'none';
		// 이벤트리스너 달아준다.	
	newInput4.addEventListener('change', function(e){
			let file = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file);
	
			reader.onload = function(e){
			img4.src = e.target.result;
			img4.style.display = 'block';
			}
	
	});
		
	document.querySelector('#register-image-div-4').append(newInput4);

	// 이미지 감춘다. 
	img4.style.display = 'none';
	
});
document.querySelector('#f5').addEventListener('click', function(e){
	
	let img5 = document.querySelector('#img5');
	// input 태그 갈아낀다. 
	document.querySelector('#d5').remove();	
	let newInput5 = document.createElement('input');
	newInput5.type = 'file';
	newInput5.id = 'd5';
	newInput5.name = 'sub4';
	newInput5.style.display = 'none';
		// 이벤트리스너 달아준다.	
	newInput5.addEventListener('change', function(e){
			let file = e.target.files[0];
			let reader = new FileReader();
			reader.readAsDataURL(file);
	
			reader.onload = function(e){
			img5.src = e.target.result;
			img5.style.display = 'block';
			}
	
	});
		
	document.querySelector('#register-image-div-5').append(newInput5);

	// 이미지 감춘다. 
	img5.style.display = 'none';
	
});

