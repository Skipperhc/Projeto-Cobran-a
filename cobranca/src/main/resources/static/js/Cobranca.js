$(function() {
	$('[rel="tooltip"]').tooltip();
	$('.js-currency').maskMoney({decimal: ',', thousands: '.', allowZero: true});
	$('.js-atualizar-status').on('click', function(event){
		event.preventDefault();
		
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href')
		
		var response = $.ajax({
			 url: urlReceber,
			 type: 'PUT'
		});
		response.done(function(e) {
			var codigoTitulo = botaoReceber.data('codigo');
			$("[data-role=" + codigoTitulo + "]").html('<span class="badge p-2 pl-4 pr-4 badge-success">'+ e +'</span>');
			botaoReceber.hide();
		})
		
		response.fail(function(e) {
			console.log(e);
			alert('Erro recebendo cobrança')
		})
	});
});